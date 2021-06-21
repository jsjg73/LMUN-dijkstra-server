package com.mycom.navigation.bus.reader;

import java.util.List;

import org.apache.commons.collections4.map.MultiKeyMap;


public interface BusInfraReader {
	public List<String[]> readBusStopByRoute();
	public MultiKeyMap<String, String> loadRealPath();
}
