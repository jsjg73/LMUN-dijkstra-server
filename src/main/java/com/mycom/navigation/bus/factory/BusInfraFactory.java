package com.mycom.navigation.bus.factory;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.map.MultiKeyMap;

import com.mycom.navigation.bus.dto.Bus;
import com.mycom.navigation.bus.dto.BusNode;
import com.mycom.navigation.bus.reader.BusInfraReader;
import com.mycom.navigation.bus.section.BusSection;
import com.mycom.navigation.infra.InfraNode;

public class BusInfraFactory {
	private volatile static BusInfra busInfra;
	/*
	 * infs[] { 노선ID, 노선명, 순번, NODE_ID, ARS-ID, 정류소명, X좌표, Y좌표 }
	 */
	private final int BUS_ID = 0, BUS_NM = 1, ORDER = 2, NODE_ID = 3, ARS_ID = 4, STATION_NM = 5, X = 6, Y = 7;
	private final int WEIGHT_NON_STOP=6, WEIGHT_FOR_WALKING=3 ;
	
	private BusInfraReader reader;
	public BusInfraFactory(BusInfraReader reader) {
		this.reader = reader;
	}
	
	public BusInfra createOnlyOnce() {
		if(busInfra==null) {
			initiateOnlyOnce();
		}
		return busInfra;
	}
	
	private void initiateOnlyOnce() {
		synchronized (BusInfraFactory.class) {
			if(busInfra==null) {
				busInfra = BusInfra.create();
				construct();
			}
		}
	}
	
	public void construct() {
		List<String[]> busStopByRoute = reader.readBusNodeByRoute();
		MultiKeyMap<String, String> realPathsBetweenStops = reader.loadRealPath();
		// pre - next 로 검색할 수 있도록 자료구조 수정
		
		int val =1;
		String realPath = null ;
		BusNode previousStop = null;
		for(String[] record : busStopByRoute) {
			addBus(record);
			addBusNode(record);
			
			//connect
			BusNode currentStop = (BusNode) busInfra.getNode(record[NODE_ID]);
			if("1".equals(record[ORDER])) {
				previousStop = currentStop;
			}else {
				String currRealPath =realPathsBetweenStops.get(previousStop.getNodeId(), currentStop.getNodeId());
				if(realPath ==  null) {
					realPath = currRealPath;
				}else {
					realPath +=","+ currRealPath;
				}
				if(!currentStop.isEnable()){
					val+=WEIGHT_NON_STOP;
				}else {
					// 경로 추가 
					previousStop.addNextNode(currentStop, val, realPath);
					previousStop = currentStop;
					val = 1;
					realPath = null;
				}
			}
		}
		busInfra.createIndexingStructure();
		
		initBusSection();
		
		transferOnFootIterating();
		
	}

	private void transferOnFootIterating() {
		Iterator<InfraNode> iter = busInfra.nodesIterator();
		while(iter.hasNext()) {
			InfraNode curr = iter.next();
			transferOnFoot(curr);
		}
	}

	private void transferOnFoot(InfraNode curr) {
		Set<InfraNode> nodesNearbyCurr = busInfra.nodesNearby(curr.getX(), curr.getY());
		for(InfraNode node : nodesNearbyCurr) {
			if(curr.connected(node))continue;
			curr.addNextNode(node, WEIGHT_FOR_WALKING, "");
		}
	}

	private void initBusSection() {
		BusSection section = new BusSection();
		section.initialization(busInfra);
		busInfra.setSection(section);
	}

	private void addBus(String[] record) {
		String busId = record[BUS_ID];
		if (busId == null)
			throw new IllegalArgumentException("버스 아이디는 null이 될 수 없습니다.");

		Bus bus = null;
		if (!busInfra.hasBus(busId)) {
			bus = Bus.builder().id(record[BUS_ID]).name(record[BUS_NM]).build();
			busInfra.addBus(bus);
		}
	}

	int nodesSize = 0;

	private void addBusNode(String[] record) {
		String stopId = record[NODE_ID];
		if (stopId == null)
			throw new IllegalArgumentException("버스정류장 아이디는 null이 될 수 없습니다.");

		BusNode stop = null;
		if (!busInfra.hasBusNode(stopId)) {
			stop = new BusNode(nodesSize++, 
					record[NODE_ID], 
					record[ARS_ID], 
					record[STATION_NM], 
					Double.parseDouble(record[X]),
					Double.parseDouble(record[Y]));
			busInfra.addNode(stop);
		}
	}

}
