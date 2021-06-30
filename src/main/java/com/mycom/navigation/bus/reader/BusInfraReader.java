package com.mycom.navigation.bus.reader;

import java.util.List;

import org.apache.commons.collections4.map.MultiKeyMap;


public interface BusInfraReader {
	public List<String[]> readExternalBusData();
	public MultiKeyMap<String, String> loadRealPath();
}
