package com.mycom.navigation.bus.section;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RC {
	private int row;
	private int col;
	
	public RC(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
}
