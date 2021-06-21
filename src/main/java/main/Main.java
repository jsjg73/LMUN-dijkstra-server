package main;

import java.io.IOException;
import java.util.List;

import com.mycom.navigation.bus.factory.BusInfra;
import com.mycom.navigation.bus.factory.BusInfraFactory;
import com.mycom.navigation.bus.navi.Navigation;
import com.mycom.navigation.bus.reader.BusInfraReader;
import com.mycom.navigation.bus.reader.BusInfraTxtReader;

public class Main {
	public static void main(String[] args) throws IOException {
		
		// 버스 인프라 구축
		BusInfraReader bifReader = new BusInfraTxtReader();
		BusInfra bif = BusInfraFactory.construct(bifReader, 500, 500);
		
		//다익스트라
		Navigation navi = new Navigation(bif);
		//100100286	5712	34	115000006	16006	등촌중학교 백석초등학교	126.8605239274	37.5528647439
		//100100286	5712	43	113000423	14013	서교동	126.9196070234	37.5536161851
//		String re = navi.navigate(126.8605239274, 37.5528647439, 126.9196070234, 37.5536161851);
		List<String> re = navi.navigate(126.8605239274, 37.5528647439, 127.0456374655,	37.6887853785);
		if(re == null) {
			System.out.println("결과 없음");
		}else {
			for(String str : re) {
				System.out.println(str);
			}
		}
	}

}
