package com.mycom.navigation.bus.dto;

import lombok.Setter;

import java.util.Objects;

import lombok.Getter;

@Getter
@Setter
public class Edge {

	private int cost;
	private String realPath;
	
	public Edge(int cost, String realPath) {
		this.cost = cost;
		this.realPath = realPath;
	}
}
