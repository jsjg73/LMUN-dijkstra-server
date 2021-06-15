package com.mycom.navigation.bus.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mycom.navigation.bus.dto.Bus;
import com.mycom.navigation.bus.dto.BusStation;
import com.mycom.navigation.bus.reader.BusInfraReader;
import com.mycom.navigation.bus.section.BusSection;

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
//	private Set<Edge> edgeSet = new HashSet<Edge>();
	private Map<String, HashMap<String, String>> realPathData;
	private BusSection section;
	
	protected BusInfra () {	}
	// 버스 인프라 구축
	 
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private BusStation preStation;
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private int val=1;
	
	public boolean hasBus(String id) {
		return busTbl.get(id)!=null;
	}
	public boolean hasBusStation(String id) {
		return busStationTbl.get(id)!=null;
	}
	public int busStaionSize() {
		return busStationTbl.size();
	}
	
	public BusStation getBusStation(String stationId) {
		return busStationTbl.get(stationId);
	}
	
	public void loadRealPath(BusInfraReader reader) {
		realPathData = new HashMap<String, HashMap<String,String>>(); 
		
		List<String[]> listRealPath = reader.loadRealPath();
		for(String[] arr : listRealPath) {
			String from = arr[0];
			String to = arr[1];
			BusStation f = busStationTbl.get(from);
			f.addRealPath(to, arr[2]);
		}
	}
	private boolean containsBus(String busId) {
		return busTbl.containsKey(busId);
	}
	private BusStation containsBusStation(String stationId) {
		return busStationTbl.get(stationId);
	}
	public void addBus(Bus bus) {
		busTbl.put(bus.getId(), bus);
	}
	public void addBusStation(BusStation station) {
		busStationTbl.put(station.getNodeId(), station);
	}
	
	public BusStation[] stationArray() {
		BusStation[] bsArr = new BusStation[busStationTbl.size()];
		for(Map.Entry<String, BusStation> ent : busStationTbl.entrySet()) {
			bsArr[ent.getValue().getIdx()]=ent.getValue();
		}
		return bsArr;
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
