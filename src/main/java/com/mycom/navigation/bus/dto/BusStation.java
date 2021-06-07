package com.mycom.navigation.bus.dto;

import java.util.HashSet;
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
	private Set<BusStation> nextSet;
	private Set<BusStation> arroundSet;
	private int sectionIndex;
	
	public boolean addNext(BusStation station) {
		if(station == null)return false;
		if(nextSet == null)nextSet = new HashSet<BusStation>();
		return nextSet.add(station);
	}
	public boolean addArround(BusStation station) {
		if(station == null)return false;
		if(arroundSet == null)arroundSet = new HashSet<BusStation>();
		return arroundSet.add(station);
	}
	
}
