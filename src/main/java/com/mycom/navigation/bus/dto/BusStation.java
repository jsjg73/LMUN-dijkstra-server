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
	
	private Bus bus;
	private String nodeId;
	private String arsId;
	private String name;
	private String x;
	private String y;
	private Set<BusStation> nextSet;
	
	public boolean add(BusStation station) {
		if(station == null)return false;
		if(nextSet == null)nextSet = new HashSet<BusStation>();
		return nextSet.add(station);
	}
	
}
