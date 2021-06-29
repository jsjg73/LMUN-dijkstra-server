//package com.mycom.navigation.bus.writer;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//
//import com.mycom.navigation.bus.dto.BusStop;
//import com.mycom.navigation.bus.dto.Edge;
//import com.mycom.navigation.bus.factory.BusInfra;
//import com.mycompany.myapp.naver.NPath;
//import com.mycompany.myapp.naver.NaverAPI;
//import com.mycompany.myapp.naver.NaverJsonParsing;
//
//public class BusInfraTxtWriter {
////	public void writeEdges(BusInfra bif) {
////		try {
////			FileOutputStream file = new FileOutputStream("C:/workspace/practice/ReadExcelFile/Edges.txt");
////			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(file));
////			
////			Iterator<Edge> it = bif.getEdgeSet().iterator();
////			while(it.hasNext()) {
////				Edge e = it.next();
////				bw.write("0\t" );
////				bw.write(e.getFrom().getNodeId());
////				bw.write("\t");
////				bw.write(e.getTo().getNodeId());
////				bw.write("\t\n");;
////			}
////			
////			bw.flush();
////			bw.close();
////			file.close();
////		} catch (FileNotFoundException e) {
////			e.printStackTrace();
////		} catch (IOException e1) {
////			e1.printStackTrace();
////		}
////		
////	}
//
//	public void writePath(int amount, Map<String, BusStop> busStopTbl) {
//		try {
//			FileInputStream file = new FileInputStream("C:/workspace/practice/ReadExcelFile/Edges.txt");
//			BufferedReader br = new BufferedReader(new InputStreamReader(file));
//			ArrayList<String> list = new ArrayList<String>();
//			String line = null;
//			while ((line = br.readLine()) != null) { // EoF
//				list.add(line);
//			}
//			br.close();
//			file.close();
//
//			FileOutputStream fos1 = new FileOutputStream("C:/workspace/practice/ReadExcelFile/Edges.txt");
//			FileOutputStream fos2 = new FileOutputStream("C:/workspace/practice/ReadExcelFile/EdgeData.txt", true);
//			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos1));
//			BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(fos2));
////			NaverAPI naver = new NaverAPI();
////			정류장 번호를 통해 좌표 요청 to BusInfra
////			api 요청
////			data 핸들링
//			NaverAPI naver = new NaverAPI();
//
//			for (String li : list) {
//				if (amount == 0) {
//					bw.write(li + "\n");
//					continue;
//				}
//				String[] info = li.split("\t");
//
//				// 경로 받아온 edge
//				if ("1".equals(info[0])) {
//					bw.write(li + "\n");
//
//					// 경로 안 받아온 edgs
//				} else {
//					amount--;
//					// api
//
//					// 출발 도착 정류소 찾기
//					BusStop from = busStopTbl.get(info[1]);
//					BusStop to = busStopTbl.get(info[2]);
//
//					// api 요청 재료 세팅
//					NPath np = new NPath();
//					np.setEx(to.getX() + "");
//					np.setEy(to.getY() + "");
//					np.setSx(from.getX() + "");
//					np.setSy(from.getY() + "");
//
//					// api 요청
//					naver.getPath(np);
//
//					// path배열만 추출
//					NaverJsonParsing njp = new NaverJsonParsing();
//					njp.polyPathParsing(np);
//
//					String data = "null";
//					if (np.getPath() != null) {
////						data = np.getPath().toString();
//					} else {
//						data = np.getStMsg();
//					}
//
//					bw2.write(info[1]);
//					bw2.write("\t");
//					bw2.write(info[2]);
//					bw2.write("\t");
//					bw2.write(data + "\n");
//
//					bw.write("1");
//					bw.write(li.substring(1) + "\n");
//					System.out.println(amount + "번 완료 ::" + np.getStCode() + " :: " + np.getStMsg());
//				}
//			}
//			bw2.flush();
//			bw2.close();
//			bw.flush();
//			bw.close();
//			if (amount > 0) {
//				System.out.println("all path are searched");
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void writeSectionImage(BusInfra bif) throws IOException {
//		Set<BusStop>[][] sections = bif.getSectionArray();
//		FileOutputStream file = new FileOutputStream("C:/workspace/practice/ReadExcelFile/sectionImage.txt");
//		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(file));
//		for (int i = 0; i < sections.length; i++) {
//			for (int j = 0; j < sections[i].length + 2; j++) {
//				if (sections[j + i * 500] != null) {
//					int size = sections[i][j].size();
//					String s = size + ",";
//					bw.write(s);
//				} else {
//					bw.write("0,");
//				}
//			}
//			bw.write("\n");
//			bw.flush();
//		}
//		bw.close();
//	}
//}
