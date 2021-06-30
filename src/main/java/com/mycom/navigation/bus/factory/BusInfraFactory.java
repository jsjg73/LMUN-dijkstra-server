package com.mycom.navigation.bus.factory;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.keyvalue.MultiKey;
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
	private List<String[]> busNodesFromExternalData;
	private MultiKeyMap<String, String> realPathsBetweenNodes;
	public void construct() {
		
		readExternalBusInformation();
		
		addDataToInfra();
		
		busInfra.createIndexingStructure();
		
		initBusSection();
		
		transferOnFootIterating();
		
	}
	
	private void readExternalBusInformation() {
		busNodesFromExternalData = reader.readExternalBusData();
		realPathsBetweenNodes = reader.loadRealPath();
	}
	
	private void addDataToInfra() {
		
		for(String[] record : busNodesFromExternalData) {
			addData(record);
			extendConnectionBetweenNodes(record);
		}		
	}
	
	

	private void addData(String[] record) {
		addBus(record);
		addBusNode(record);
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
	
	private int weight =1;
	private String realPath = null ;
	private BusNode previousNode = null;
	private BusNode currentNode = null;
	
	private void extendConnectionBetweenNodes(String[] record) {
		currentNode = (BusNode) busInfra.getNode(record[NODE_ID]);
		if(isFirstNode(record[ORDER])) {
			previousNode = currentNode;
		}else {
			connect();
		}
	}

	private boolean isFirstNode(String order) {
		return "1".equals(order);
	}
	
	private void connect() {
			updateRealPath();
			connectNodes();
	}
	
	private void updateRealPath() {
		String currRealPath =realPathsBetweenNodes.get(previousNode.getNodeId(), currentNode.getNodeId());
		if(realPath !=  null) 
			realPath +=",";
		realPath += currRealPath;
	}
	
	//TODO 부수효과 제거
	//cleancode54 : 부수효과를 일으키지마라 위배
	private void connectNodes() {
		if(!currentNode.isEnable()){
			weight+=WEIGHT_NON_STOP;
		}else {
			// 경로 추가 
			previousNode.addNextNode(currentNode, weight, realPath);
			previousNode = currentNode;
			weight = 1;
			realPath = null;
		}
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

	

}
