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
	private String nodeId;
	private String arsId;
	private String name;
	private double x;
	private double y;
	private Map<BusStation, Integer> next;
	private int sectionIndex;
	
	private int sectionRow;
	private int sectionCol;
	
	public void addNext(BusStation station, int cost) {
		if(station == null)return;
		if(next == null)next = new HashMap<BusStation, Integer>();
		if(next.containsKey(station)) {
			cost = Math.min(cost, next.get(station));
		}
		next.put(station, cost);
	}
	public Set<Entry<BusStation, Integer>> getNextEntry() {
		return next.entrySet();
	}
	public boolean unusedStation() {
		return name.contains("(가상)") || name.contains("(경유)");
	}
	
}
