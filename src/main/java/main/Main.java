package main;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.mycom.navigation.bus.BusInfra;
import com.mycom.navigation.bus.dto.BusStation;
import com.mycom.navigation.bus.navi.Navigation;
import com.mycom.navigation.bus.reader.BusInfraTxtReader;
import com.mycom.navigation.bus.writer.BusInfraTxtWriter;
import com.mycompany.myapp.naver.Distance;
import com.mycompany.myapp.naver.NPath;
import com.mycompany.myapp.naver.NaverAPI;

public class Main {
	public static void main(String[] args) throws IOException {
		
		// 버스 인프라 구축
		BusInfra bif = new BusInfra(new BusInfraTxtReader(),500, 500);
		
		// 버스 정류장 영역 구분 
		// 500칸*500칸, (대략, 가로 140m * 세로 120m)
		bif.dividingIntoArea();
		
		//다익스트라
		Navigation navi = new Navigation(bif);
		//100100286	5712	34	115000006	16006	등촌중학교 백석초등학교	126.8605239274	37.5528647439
		//100100286	5712	43	113000423	14013	서교동	126.9196070234	37.5536161851
//		String re = navi.navigate(126.8605239274, 37.5528647439, 126.9196070234, 37.5536161851);
		String re = navi.navigate(126.8605239274, 37.5528647439, 127.0456374655,	37.6887853785);
		System.out.println(re);
	}

}
