package main;

import com.mycom.navigation.bus.BusInfra;
import com.mycom.navigation.bus.reader.BusInfraTxtReader;
import com.mycom.navigation.bus.writer.BusInfraTxtWriter;
import com.mycompany.myapp.naver.NPath;
import com.mycompany.myapp.naver.NaverAPI;

public class Main {
	// C:\workspace\practice\ReadExcelFile\BusStation.xlsx
	public static void main(String[] args) {
		
		BusInfra bif = new BusInfra(new BusInfraTxtReader());
//		new BusInfraTxtWriter().writeEdges(bif);;
//		new BusInfraTxtWriter().writePath(6000, bif.getBusStationTbl());
		System.out.println("done");
		// 7.3	MB
//		NPath np = new NPath();
//		100100124	0017	7	102000216	03310	남이장군사당	126.9579568939	37.5367069612
//		100100124	0017	8	102000218	03312	새마을금고	126.9595027769	37.5371259582
		
//		중앙대후문	126.9535640433	37.5050079316
//		이화약국	126.9504762701	37.50495608

//		np.setEx("126.9504762701");
//		np.setEy("37.50495608");
//		np.setSx("126.9535640433");
//		np.setSy("37.5050079316");
//		NaverAPI naver = new NaverAPI();
//		naver.getPath(np);
//		
//		System.out.println(np.getRawPath());
	}

}
