package com.mycom.navigation.infra;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfraEdge {
	private int cost;
	private boolean enable;
	private String realPath;
	
	public InfraEdge(int cost, String realPath) {
		this.cost = cost;
		this.realPath = realPath;
		enable = realPath!=null;
	}
	
	public void setRealPath(String realPath) {
		this.realPath = realPath;
		enable = realPath!=null;
	}
}
