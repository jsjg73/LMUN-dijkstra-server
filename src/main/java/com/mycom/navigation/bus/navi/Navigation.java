package com.mycom.navigation.bus.navi;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import com.mycom.navigation.bus.BusInfra;
import com.mycom.navigation.bus.dto.Bus;
import com.mycom.navigation.bus.dto.BusStation;

public class Navigation {
	int[] dr = {0,-1,-1,0,1,1,1,0,-1};
	int[] dc = {0,0,1,1,1,0,-1,-1,-1};
	private BusInfra bif;
	public Navigation(BusInfra bif) {
		if(bif == null) {
			throw new NullPointerException("Businfra instance cannot be null");
		}
		this.bif=bif;
	}
	private String navigate(BusStation sp, BusStation ep) {
		//
		// 영속성 계층에서 real path 받아오기
		return null;
	}
	
	public String navigate(double sx, double sy, double ex, double ey) {
		Set<BusStation> startStaions = nearStations(sx, sy) ;
		Set<BusStation> endStations = nearStations(ex, ey);
		BusStation[] bsArr = new BusStation[bif.stationSize()];
		for(Map.Entry<String, BusStation> ent : bif.getBusStationTbl().entrySet()) {
			bsArr[ent.getValue().getIdx()]=ent.getValue();
		}
		
		boolean[] v = new boolean[bsArr.length];
		int[][] v_info = new int[v.length][2]; // 비용, 직전위치
		
		PriorityQueue<Dijk> pq = new PriorityQueue<Dijk>();
		for(BusStation bs : startStaions) {
//			v_info[bs.getIdx()][1] = -1;
			pq.offer(new Dijk(0,bs.getIdx(),-1));
		}
		
		BusStation endpoint = null;
		
		while(!pq.isEmpty()){
			Dijk d = pq.poll();
			if(v[d.now])continue;
			BusStation now = bsArr[d.now];
//			System.out.println(now.getName());
			v[d.now]=true;
			v_info[d.now][0] = d.cost ;
			v_info[d.now][1] = d.before ;
			if(endStations.contains(bsArr[d.now])) {
				endpoint = bsArr[d.now];
				break;
			}
			// 버스로 정류장 이동.
			if(now.getNextSet()!=null) {
				for(Entry<BusStation,Integer> ent : now.getNextEntry()) {
					BusStation bs = ent.getKey();
					int value = ent.getValue();
					
					//방문 기록 없는 정류장
					if(!v[bs.getIdx()]) {
						if(intersection(now, bs)) { 
							// 환승 아닐 때 value+0 
							pq.offer(new Dijk(d.cost+value, bs.getIdx(), d.now));
						}else {	
							//정류장 내 환승일 때 value+3
							pq.offer(new Dijk(d.cost+value+3, bs.getIdx(), d.now));
						}
						
					}
				}
			}
			// 걸어서 정류장 이동
			int[] rc = bif.convertIdx2RC(now.getSectionIndex());
			int r = rc[0];
			int c = rc[1];
			for(int i=0; i<=8; i++) {
				int mr = r + dr[i];
				int mc = c + dc[i];
				Set<BusStation> near = bif.busStaionsInSection(mr, mc);
				if(near != null) {
					for(BusStation b : near) {
						if(b.unusedStation())continue; // 경유 또는 가상 정류장
						if(!v[b.getIdx()]) {
							// 정류장 외 환승 +6
							pq.offer(new Dijk(d.cost+6, b.getIdx(), d.now));
						}
					}
				}
			}
		}
		
		if(endpoint!=null) {
			int idx = endpoint.getIdx();
			while(idx != -1) {
				System.out.println(idx+" "+v_info[idx][0]+" "+bsArr[idx].getName()+"\t");
				idx = v_info[idx][1];
			}
			
			return endpoint.getName();
		}
		else
			return "결과를 찾지 못했다";
	}
	class Dijk implements Comparable<Dijk>{
		int cost;
		int now;
		int before;
		
		public Dijk(int cost, int now, int before) {
			this.cost = cost;
			this.now = now;
			this.before = before;
		}

		@Override
		public int compareTo(Dijk o) {
			return this.cost-o.cost;
		}

	}
	private Set<BusStation> nearStations(double x, double y) {
		boolean[][] v = new boolean[bif.getRow()+2][bif.getCol()+2];
		int r = bif.cvtX2R(x);
		int c = bif.cvtY2C(y);
		
		Queue<int[]> que = new LinkedList<int[]>();
		que.offer(new int[] {r,c});
		
		while(!que.isEmpty()) {
			int[] arr = que.peek();
			r = arr[0];
			c = arr[1];
			if(v[r][c])continue;
			v[r][c] =true;
			if(bif.existStionInSection(r,c))break;
			for(int i=1; i<=8; i++) {
				int mr = r + dr[i];
				int mc = c + dc[i];
				if(posible(r,c,v) && !v[mr][mc]) {
					que.offer(new int[] {mr,mc});
				}
			}
		}
		
		return bif.busStaionsInSection(r,c);
	}
	private boolean posible(int r, int c, boolean[][] v) {
		return r>=0 && r < v.length && c>=0 && c<v[0].length;
	}
	private boolean intersection(BusStation f, BusStation t) {
		for(Bus fb : f.getBuses()) {
			if(t.existBus(fb))return true;
		}
		return false;
	}
}
