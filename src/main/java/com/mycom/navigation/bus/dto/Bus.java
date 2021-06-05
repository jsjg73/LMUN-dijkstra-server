package com.mycom.navigation.bus.dto;

import java.util.Map;

import com.mycom.navigation.bus.BusInfra;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Bus {
	private String id;
	private String name;
}
