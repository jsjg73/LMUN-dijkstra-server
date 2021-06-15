package com.mycom.navigation.bus.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.mycom.navigation.bus.factory.BusInfra;
/*
 * BusInfra 구축하기 위해 서울시 버스 노선 및 정류장 정보 텍스트 파일 읽기.
 * 
 * txt파일 의 요구 형식
 * 	각 행의 정보 및 순서 : 노선ID, 노선명, 순번, NODE_ID, ARS-ID, 정류소명, X좌표, Y좌표
 *  구분자  : \t
 *   
 * */
public class BusInfraTxtReader implements BusInfraReader{

	public List<String[]> readBusStopByRoute() {
		List<String[]> busStopByRoute = new ArrayList<String[]>();
		try {
			FileInputStream busStopByRouteFile = new FileInputStream("C:/workspace/practice/ReadExcelFile/Stations.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(busStopByRouteFile));
			String line = null;
			while((line = br.readLine())!= null) { // EoF
				String[] infs = line.split("\t");
				busStopByRoute.add(infs);
			}
			busStopByRouteFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return busStopByRoute;
	}
	public List<String[]> loadRealPath() {
		FileInputStream file;
		List<String[]> list = new ArrayList<String[]>();
		try {
			file = new FileInputStream("C:/workspace/practice/ReadExcelFile/EdgeData.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(file));
			String line = null;
			while((line = br.readLine())!= null) { // EoF
				String[] infs = line.split("\t");
				list.add(infs);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
