package main;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.mycom.navigation.bus.BusInfra;
import com.mycom.navigation.bus.dto.BusStation;
import com.mycom.navigation.bus.reader.BusInfraTxtReader;
import com.mycom.navigation.bus.writer.BusInfraTxtWriter;
import com.mycompany.myapp.naver.Distance;
import com.mycompany.myapp.naver.NPath;
import com.mycompany.myapp.naver.NaverAPI;

public class Main {
	public static void main(String[] args) throws IOException {
		
		// 버스 인프라 구축
		BusInfra bif = new BusInfra(new BusInfraTxtReader());
		
		// 버스 정류장 영역 구분 
		// 500칸*500칸, (대략, 가로 140m * 세로 120m)
		bif.dividingIntoArea(500, 500);
		
		//다익스트라
	}

}
