package com.mycom.navigation.bus.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BusStop {
	private int idx;
	private String nodeId;
	private String arsId;
	private String name;
	private double x;
	private double y;
//	private Map<BusStop, Integer> next;
//	private Map<String, String> realPathTbl;
	private Map<BusStop, Edge> nextStops;
	private int sectionIndex;
	
	private int sectionRow;
	private int sectionCol;
	public void addNextStop(BusStop stop, int cost, String realPath) {
		if(stop == null)return;
		if(nextStops == null)nextStops = new HashMap<BusStop, Edge>();
		Edge e = nextStops.get(stop);
		if(e == null) {
			e = new Edge(cost, realPath);
			nextStops.put(stop, e);
		}else {
			if(e.getCost() > cost) {
				e.setCost(cost);
				e.setRealPath(realPath);
			}
		}
	}
	public String findRealpath(BusStop next) {
		return nextStops.get(next).getRealPath();
	}
	public Set<Entry<BusStop, Edge>> getNextEntry() {
		return nextStops.entrySet();
	}
	public boolean unusedStop() {
		return name.contains("(가상)") || name.contains("(경유)");
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null  || getClass() != obj.getClass())return false;
		
		BusStop e = (BusStop)obj;
		
		return nodeId.equals(e.getNodeId());
	}
	
	@Override
    public int hashCode() {
		return nodeId.hashCode();
    }
}
