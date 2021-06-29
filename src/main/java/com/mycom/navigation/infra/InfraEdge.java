package com.mycom.navigation.infra;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfraEdge {
	private int cost;
	private String realPath;
	
	public InfraEdge(int cost, String realPath) {
		this.cost = cost;
		this.realPath = realPath;
	}
}
