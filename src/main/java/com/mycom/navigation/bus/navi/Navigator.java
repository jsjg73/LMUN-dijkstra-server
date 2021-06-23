package com.mycom.navigation.bus.navi;

import java.util.List;

import com.mycom.navigation.infra.Infrastructure;

public abstract class Navigator {
	
	private Infrastructure infra;
	
	public Navigator(Infrastructure infra) {
		this.infra = infra;
	}
	
	public abstract List<String> navigate(double sx, double sy, double ex, double ey);
}
