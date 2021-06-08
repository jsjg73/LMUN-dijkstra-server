package com.mycom.navigation.bus.section;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class XY {
	private double x;
	private double y;
	
	public XY(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void max(double a, double b) {
		x=Math.max(x, a);
		y=Math.max(y, b);
	}
	public void min(double a, double b) {
		x=Math.min(x, a);
		y=Math.min(y, b);
	}
}
