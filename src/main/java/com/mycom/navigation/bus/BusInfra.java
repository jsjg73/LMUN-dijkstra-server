package com.mycom.navigation.bus;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.mycom.navigation.bus.dto.Bus;
import com.mycom.navigation.bus.dto.BusStation;
import com.mycom.navigation.bus.dto.Edge;
import com.mycom.navigation.bus.reader.BusInfraReader;
import com.mycom.navigation.bus.section.BusSection;
import com.mycom.navigation.bus.section.RC;
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
	
	private BusSection section;
	
	public BusInfra (int row, int col) {
		section = new BusSection(row, col);
	}
	
	// 버스 인프라 구축
	public void constructInfra(BusInfraReader reader) {
		reader.readBusInfra(this);
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
										.idx(busStationTbl.size())
										.nodeId(infs[NODE_ID])
										.arsId(infs[ARS_ID])
								 		.name(infs[STATION_NM])
										.x(parseDouble(infs[X]))
										.y(parseDouble(infs[Y]))
										.build();
			putBusStation(station);
			section.minMaxXY(station);
		}
		
		// 인접 정류장 추가
		if("1".equals(infs[ORDER])) {
			preStation = station;
		}else if(station.unusedStation()){//가상 정류장
			val+=3;
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
	
	public BusStation[] stationArray() {
		BusStation[] bsArr = new BusStation[busStationTbl.size()];
		for(Map.Entry<String, BusStation> ent : busStationTbl.entrySet()) {
			bsArr[ent.getValue().getIdx()]=ent.getValue();
		}
		return bsArr;
	}
	
	public void dividingIntoArea() {
		section.dividingIntoArea(busStationTbl);
	}
	public Set<BusStation> arrounStations(double x, double y){
		return section.arrounStations(x, y);
	}
	public Set<BusStation> stationsInSection(int r, int c){
		return section.stationsInSection(r,c);
	}
	
	public Set<BusStation>[][] getSectionArray(){
		return section.getSections();
	}
}
