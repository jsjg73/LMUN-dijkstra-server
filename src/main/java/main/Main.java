package main;

import java.io.IOException;
import java.util.List;

import com.mycom.navigation.bus.factory.BusInfra;
import com.mycom.navigation.bus.factory.BusInfraFactory;
import com.mycom.navigation.bus.navi.DijkstraNavigator;
import com.mycom.navigation.bus.navi.Navigator;
import com.mycom.navigation.bus.reader.BusInfraReader;
import com.mycom.navigation.bus.reader.BusInfraTxtReader;

public class Main {
	public static void main(String[] args) throws IOException {
		
		// 버스 인프라 구축
		BusInfraReader bifReader = new BusInfraTxtReader();
		
		BusInfra bif =  new BusInfraFactory(bifReader).createOnlyOnce();
		
		Navigator dijkstraNavi = new DijkstraNavigator(bif);
		
		List<String> realPaths = dijkstraNavi.navigate(126.8605239274, 37.5528647439, 127.0456374655, 37.6887853785);
		
		if(realPaths == null) {
			System.out.println("경로 찾기 실패");
		}else {
			print(realPaths);
		}
		
	}

	private static void print(List<String> realPaths) {
		for(String str : realPaths) {
			System.out.println(str);
		}		
	}

}
