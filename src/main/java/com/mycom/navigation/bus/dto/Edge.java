package com.mycom.navigation.bus.dto;

import lombok.Setter;

import java.util.Objects;

import lombok.Getter;

@Getter
@Setter
public class Edge {
	
	public Edge(BusStation from, BusStation to) {
		super();
		this.from = from;
		this.to = to;
	}

	private BusStation from;
	private BusStation to;
	private String path;
	
	/*시간, 거리*/
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null  || getClass() != obj.getClass())return false;
		
		Edge e = (Edge)obj;
		
		return e.hashStr().equals(hashStr());
	}
	
	@Override
    public int hashCode() {
		return hashStr().hashCode();
    }
	public String hashStr() {
		return this.from.getNodeId()+this.to.getNodeId();
	}
}
