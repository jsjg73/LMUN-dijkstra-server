package com.mycom.navigation.bus.reader;

import java.util.List;

import com.mycom.navigation.bus.factory.BusInfra;

public interface BusInfraReader {
	public List<String[]> readBusStopByRoute();
	public List<String[]> loadRealPath();
}
