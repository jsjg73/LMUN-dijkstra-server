package com.mycom.navigation.bus.factory;

import java.util.List;

import com.mycom.navigation.bus.dto.Bus;
import com.mycom.navigation.bus.dto.BusStation;
import com.mycom.navigation.bus.reader.BusInfraReader;
import com.mycom.navigation.bus.section.BusSection;

public class BusInfraFactory {
	private static BusInfra busInfra = new BusInfra();
	/* infs[]
	 * { 노선ID, 노선명, 순번, NODE_ID, ARS-ID, 정류소명, X좌표, Y좌표 }
	 * */
	private static final int BUS_ID=0, BUS_NM=1, ORDER=2, NODE_ID=3, ARS_ID=4, STATION_NM=5, X=6, Y=7;
	
	public static BusInfra construct(BusInfraReader reader) {
		int defaultSectionRow = 500;
		int defaultSectionCol = 500;
		return construct(reader,defaultSectionRow, defaultSectionCol);
	}
	
	public static BusInfra construct(BusInfraReader reader, int sectionRow, int sectionCol) {
		List<String[]> busStopByRoute = reader.readBusStopByRoute();
		int val =1;
		BusStation previousStation = null;
		for(String[] record : busStopByRoute) {
			addBus(record);
			addBusStation(record);
			
			//connect
			BusStation currentStation = busInfra.getBusStation(record[NODE_ID]);
			if("1".equals(record[ORDER])) {
				previousStation = currentStation;
			}else if(currentStation.unusedStation()){
				val+=3;
			}else {
				previousStation.addNext(currentStation, val);
				previousStation = currentStation;
				val = 1;
			}
		}
		initBusSection(sectionRow, sectionCol);
		return busInfra;
	}
	private static void initBusSection(int sectionRow, int sectionCol) {
		BusSection section = new BusSection(sectionRow, sectionCol);
		section.initialization(busInfra.getBusStationTbl());
		busInfra.setSection(section);
	}
	
	private static void addBus(String[] record) {
		String busId = record[BUS_ID];
		if(busId == null)throw new IllegalArgumentException("버스 아이디는 null이 될 수 없습니다.");
		
		Bus bus = null;
		if(!busInfra.hasBus(busId)) {
			bus = Bus.builder()
					.id(record[BUS_ID])
					.name(record[BUS_NM])
					.build();
			busInfra.addBus(bus);
		}
	}
	private static boolean addBusStation(String[] record) {
		String stationId = record[NODE_ID];
		if(stationId == null)throw new IllegalArgumentException("버스정류장 아이디는 null이 될 수 없습니다.");
		
		BusStation station=null;
		if(!busInfra.hasBusStation(stationId)) {
			station = BusStation.builder()
					.idx(busInfra.busStaionSize())
					.nodeId(record[NODE_ID])
					.arsId(record[ARS_ID])
			 		.name(record[STATION_NM])
					.x(parseDouble(record[X]))
					.y(parseDouble(record[Y]))
					.build();
			busInfra.addBusStation(station);
		}
		return false;
	}
	private static double parseDouble(String v) {
		return Double.parseDouble(v);
	}
}
