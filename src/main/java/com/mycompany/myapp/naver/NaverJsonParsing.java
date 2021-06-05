package com.mycompany.myapp.naver;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
public class NaverJsonParsing {
	
	
	public void polyPathParsing(NPath path) {
		JSONArray pathArray = null;
		if(path.getStCode()!=200)return;
		double goalx = Double.parseDouble(path.getEx());
		double goaly = Double.parseDouble(path.getEy());
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonOb = (JSONObject) jsonParser.parse(path.getRawPath());
			
			long result_code = (Long) jsonOb.get("code");
			
			if(result_code == 0) { // 경로 �??�� ?���?
				pathArray = new JSONArray();
				JSONObject ob = (JSONObject) jsonOb.get("route");
				JSONArray docArray = (JSONArray)(ob.get("traavoidcaronly"));
				ob = (JSONObject) docArray.get(0);
				//경로 ?��?��?�� 
				pathArray = (JSONArray) ob.get("path");
				path.setPath(pathArray);
				
			}else { 
				// 경로 �??�� ?��?��
				path.setStCode(result_code);
				path.setStMsg((String)jsonOb.get("message"));
				//			1	-	출발�??? ?��착�?�? ?��?��
				//			2	-	출발�? ?��?�� ?��착�?�? ?���? 주�??�� ?��?�� 경우
				//			3	-	?��?���? 길찾�? 결과 ?���? 불�?
				//			4	-	경유�?�? ?���? 주�??�� ?��?�� 경우
				//			5	-	?���? 경로�? 매우 �? 경우(경유�?�? ?��?��?�� 직선거리?�� ?��?�� 1500km?��?��?�� 경우)
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
