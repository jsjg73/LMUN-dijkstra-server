package com.mycom.navigation.bus.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BusStation {
	private int idx;
	private Bus bus;
	private String nodeId;
	private String arsId;
	private String name;
	private double x;
	private double y;
	private Map<BusStation, Integer> nextSet;
	private Set<Bus> buses;
	private int sectionIndex;
	
	public Integer addNext(BusStation station, int cost) {
		if(station == null)return 0;
		if(nextSet == null)nextSet = new HashMap<BusStation, Integer>();
		if(nextSet.containsKey(station)) {
			int val = nextSet.get(station);
			cost = Math.min(cost, val);
		}
		return nextSet.put(station, cost);
	}
	public Set<Entry<BusStation, Integer>> getNextEntry() {
		return nextSet.entrySet();
	}
	public boolean unusedStation() {
		return name.contains("(가상)") || name.contains("(경유)");
	}
	
	public void addBus(Bus b) {
		if(b==null)return;
		
		if(buses == null) {
			buses = new HashSet<Bus>();
		}
		buses.add(b);
	}
	public boolean existBus(Bus b) {
		return buses.contains(b);
	}
}
