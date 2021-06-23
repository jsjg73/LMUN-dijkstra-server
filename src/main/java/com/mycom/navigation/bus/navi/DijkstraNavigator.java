package com.mycom.navigation.bus.navi;

import java.util.List;

import com.mycom.navigation.infra.Infrastructure;
import com.mycom.navigation.infra.Node;

public class DijkstraNavigator extends Navigator {
	
	public DijkstraNavigator(Infrastructure infra) {
		super(infra);
	}
	
	@Override
	public List<String> navigate(double sx, double sy, double ex, double ey) {
		// TODO Auto-generated method stub

		// 출발지 근처 노드 받아오기.
		// 목적지 근처 노드 받아오기.
		
		// 다익스트라 문맥 생성
		  // 노드 리스트
		  // 방문 체크
		  // 이전 위치(역추적에 사용)
		
		// 다익스트라 구현
		
		// 역추적 : 방문 노드 리스트
		
		// realpath 리스트 생성
		return null;
	}
}
