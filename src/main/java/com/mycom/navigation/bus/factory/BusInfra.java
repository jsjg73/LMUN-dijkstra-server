package com.mycom.navigation.bus.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.MultiKeyMap;

import com.mycom.navigation.bus.dto.Bus;
import com.mycom.navigation.bus.dto.BusStop;
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
	
	private Map<String, Bus> busTbl = new HashMap<String, Bus>();
	private Map<String, BusStop> busStopTbl = new HashMap<String, BusStop>();
	private BusSection section;
	
	protected BusInfra () {	}
	// 버스 인프라 구축
	 
	public boolean hasBus(String id) {
		return busTbl.get(id)!=null;
	}
	public boolean hasBusStop(String id) {
		return busStopTbl.get(id)!=null;
	}
	public int busStaionSize() {
		return busStopTbl.size();
	}
	
	public BusStop getBusStop(String stopId) {
		return busStopTbl.get(stopId);
	}
	public void addBus(Bus bus) {
		busTbl.put(bus.getId(), bus);
	}
	public void addBusStop(BusStop stop) {
		busStopTbl.put(stop.getNodeId(), stop);
	}
	
	public BusStop[] stopArray() {
		BusStop[] bsArr = new BusStop[busStopTbl.size()];
		for(Map.Entry<String, BusStop> ent : busStopTbl.entrySet()) {
			bsArr[ent.getValue().getIdx()]=ent.getValue();
		}
		return bsArr;
	}
	
	public Set<BusStop> arrounStops(double x, double y){
		return section.arrounStops(x, y);
	}
	public Set<BusStop> stopsInSection(int r, int c){
		return section.stopsInSection(r,c);
	}
	
	public Set<BusStop>[][] getSectionArray(){
		return section.getSections();
	}
}
