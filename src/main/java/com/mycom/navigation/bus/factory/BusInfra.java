package com.mycom.navigation.bus.factory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mycom.navigation.bus.dto.Bus;
import com.mycom.navigation.bus.section.BusSection;
import com.mycom.navigation.infra.InfraNode;
import com.mycom.navigation.infra.Infrastructure;

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
public class BusInfra extends Infrastructure{
	private static BusInfra instance = new BusInfra();
	protected static BusInfra create() {
		return instance;
	}
	
	private Map<String, Bus> busTbl = new HashMap<String, Bus>();
	private BusSection section;
	private InfraNode[] nodesByIdx;
	private BusInfra () {}
	
	@Override
	public Set<InfraNode> nodesNearby(double x, double y) {
		return section.nodesNearby(x, y);
	}
	
	@Override
	public InfraNode getNodeByIndex(int idx) {
		return nodesByIdx[idx];
	}
	
	public void createIndexingStructure() {
		if(nodesByIdx != null)return;
		nodesByIdx = new InfraNode[nodeSize];
		
	}
	
	public boolean hasBus(String id) {
		return busTbl.get(id)!=null;
	}
	public boolean hasBusNode(String id) {
		return nodes.get(id)!=null;
	}
	public void addBus(Bus bus) {
		busTbl.put(bus.getId(), bus);
	}
	
}
