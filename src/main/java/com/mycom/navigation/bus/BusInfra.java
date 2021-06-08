package com.mycom.navigation.bus;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mycom.navigation.bus.dto.Bus;
import com.mycom.navigation.bus.dto.BusStation;
import com.mycom.navigation.bus.dto.Edge;
import com.mycom.navigation.bus.reader.BusInfraReader;
import com.mycom.navigation.bus.writer.BusInfraTxtWriter;
import com.mycompany.myapp.naver.Distance;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


/*
 * 서울 버스노선별 정류소 정보를 포함한 버스 인프라
 * 역할:
 * 버스, 정류장, 간선 객체 생성
 * 버스 네트워크 구축
 * 	버스 테이블에 객체 추가
 *  버스 정류장 테이블에 객체 추가
 *  연결된 정류장 정보(간선) 집합에 객체 추가
 *  걸어서 환승할 정류장 정보(간선) 집합에 객체 추가
 * */
@Getter
@Setter
public class BusInfra {
	
	/* infs[]
	 * { 노선ID, 노선명, 순번, NODE_ID, ARS-ID, 정류소명, X좌표, Y좌표 }
	 * */
	private final int BUS_ID=0, BUS_NM=1, ORDER=2, NODE_ID=3, ARS_ID=4, STATION_NM=5, X=6, Y=7;
	
	private Map<String, Bus> busTbl = new HashMap<String, Bus>();
	private Map<String, BusStation> busStationTbl = new HashMap<String, BusStation>();
	private Set<Edge> edgeSet = new HashSet<Edge>();
	
	private Set<BusStation>[] sections;
	private double minX = 999;
	private double minY = 999;
	private double maxX = 0;
	private double maxY = 0;
	private int row,col;
	
	
	
	public BusInfra (BusInfraReader reader, int row, int col) {
		// 버스 인프라 생성
		reader.readBusInfra(this);
		this.row = row;
		this.col = col;
	}
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private BusStation preStation;
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private int val=1;
	
	public void extendInfra(String[] infs) {
		Bus bus = busTbl.get(infs[BUS_ID]);
		//버스 정보 추가
		if(bus == null) {
			bus = Bus.builder()
					.id(infs[BUS_ID])
					.name(infs[BUS_NM])
					.build();
			putBus(bus);
		}
		
		//정류장 정보 추가
		BusStation station=null;
		
		if((station = containsBusStation(infs[NODE_ID]))==null) {
			station = BusStation.builder()
										.idx(stationSize())
										.nodeId(infs[NODE_ID])
										.arsId(infs[ARS_ID])
										.name(infs[STATION_NM])
										.x(parseDouble(infs[X]))
										.y(parseDouble(infs[Y]))
										.build();
			putBusStation(station);
			minX=Math.min(minX, station.getX());
			minY=Math.min(minY, station.getY());
			maxX=Math.max(maxX, station.getX());
			maxY=Math.max(maxY, station.getY());
		}
		
		// 버스 추가
		station.addBus(bus);
		
		// 인접 정류장 추가
		if("1".equals(infs[ORDER])) {
			preStation = station;
		}else if(station.unusedStation()){//가상 정류장
			val+=9;
		}else{
			preStation.addNext(station, val);
			
//			"C:/workspace/practice/ReadExcelFile/Edges.txt"에 저장할 때 사용
			edgeSet.add(new Edge(preStation, station));
			
			preStation = station;
			val =1;
		}
	}
	private boolean containsBus(String busId) {
		return busTbl.containsKey(busId);
	}
	private BusStation containsBusStation(String stationId) {
		return busStationTbl.get(stationId);
	}
	private void putBus(Bus bus) {
		busTbl.put(bus.getId(), bus);
	}
	private void putBusStation(BusStation station) {
		busStationTbl.put(station.getNodeId(), station);
	}
	
	private double parseDouble(String v) {
		return Double.parseDouble(v);
	}
	
	
	double XUnit;
	double YUnit;
	public void dividingIntoArea() throws IOException {
		if(busStationTbl == null) {
			throw new IllegalStateException("모든 버스 정류장 정보가 필요합니다.");
		}
		if(maxY==0 || maxX==0 ||minX==999|| minY==999) {
			throw new IllegalStateException("경위도의 영역 한계값 설정이 필요합니다.");
		}
		
		XUnit = ((maxX-minX)/500); // 0.0011009615529999905
		YUnit = ((maxY-minY)/500); // 0.0016079761373999873

		sections = new HashSet[(row+2)*(col+2)];
		
		for(Map.Entry<String , BusStation> ent : busStationTbl.entrySet()) {
			BusStation bs = ent.getValue();
			int sectionIdx = calSectionIndex(bs.getX(), bs.getY());
			if(sections[sectionIdx] == null) {
				sections[sectionIdx] = new HashSet<BusStation>();
			}
			sections[sectionIdx].add(bs);
			bs.setSectionIndex(sectionIdx);
		}
	}
	public int calSectionIndex(double x, double y) {
		
		int a = cvtX2R(x);
		int b = cvtY2C(y);
		
		return convertRC2Idx(a, b);
	}
	
	public int convertRC2Idx(int r, int c) {
		return c*col+r+1;
	}
	public int[] convertIdx2RC(int idx) {
		int r = (idx-1)%col;
		int c = (idx-1)/col;
		return new int[] {r,c};
	}
	public int cvtX2R(double x) {
		return (int)( (x-minX)/XUnit );
	}
	public int cvtY2C(double y) {
		return (int)( (y-minY)/YUnit );
	}
	public boolean existStionInSection(int r, int c) {
		Set<BusStation> section = busStaionsInSection(r, c);
		if(section == null)return false;
		for(BusStation bs : section) {
			if(bs.unusedStation())continue;
			return true;
		}
		return false;
	}
	public Set<BusStation> busStaionsInSection(int r, int c){
		return sections[convertRC2Idx(r, c)];
	}
	public Set<BusStation> busStaionsInSection(int sectionIdx){
		return sections[sectionIdx];
	}
	public int stationSize() {
		return busStationTbl.size();
	}
}
