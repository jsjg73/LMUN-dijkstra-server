package com.mycom.navigation.bus.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mycom.navigation.infra.InfraNode;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusNode extends InfraNode{
	public BusNode(int idx, String nodeId, String arsId, String name, double x, double y) {
		super(idx, nodeId);
		this.arsId = arsId;
		this.name = name;
		this.enable = !(name.contains("(가상)") || name.contains("(경유)"));
		this.x = x;
		this.y = y;
	}

	private String arsId;
	private String name;
	private double x;
	private double y;
	

}
