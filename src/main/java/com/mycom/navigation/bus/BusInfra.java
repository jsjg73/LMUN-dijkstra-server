package com.mycom.navigation.bus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mycom.navigation.bus.dto.Bus;
import com.mycom.navigation.bus.dto.BusStation;
import com.mycom.navigation.bus.dto.Edge;
import com.mycom.navigation.bus.reader.BusInfraReader;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


/*
 * 서울 버스노선별 정류소 정보를 포함한 버스 인프라
 * 버스 네트워크 구축
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
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private BusStation preStation;
	
	public BusInfra (BusInfraReader reader) {
		// 버스 인프라 생성
		reader.readBusInfra(this);
	}
	
	public void extendInfra(String[] infs) {
		
		//버스 정보 추가
		if(!containsBus(infs[BUS_ID])) {
			Bus bus = Bus.builder()
					.id(infs[BUS_ID])
					.name(infs[BUS_NM])
					.build();
			putBus(bus);
		}
		
		//정류장 정보 추가
		BusStation station=null;
		if((station = containsBusStation(infs[NODE_ID]))==null) {
			station = BusStation.builder()
										.nodeId(infs[NODE_ID])
										.arsId(infs[ARS_ID])
										.name(infs[STATION_NM])
										.x(infs[X])
										.y(infs[Y])
										.build();
			putBusStation(station);
		}
		
		// 간선 정보 생성
		if("1".equals(infs[ORDER])) {
			preStation = station;
		}else {
			preStation.add(station);
			
//			"C:/workspace/practice/ReadExcelFile/Edges.txt"에 저장할 때 사용
			edgeSet.add(new Edge(preStation, station));
			
			preStation = station;
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
	
}
