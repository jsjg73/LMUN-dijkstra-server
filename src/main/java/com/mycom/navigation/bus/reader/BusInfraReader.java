package com.mycom.navigation.bus.reader;

import java.util.List;

import com.mycom.navigation.bus.BusInfra;

public interface BusInfraReader {
	public void readBusInfra(BusInfra bif);
	public List<String[]> loadRealPath();
}
