package com.mycom.navigation.bus.navi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import com.mycom.navigation.bus.dto.BusStop;
import com.mycom.navigation.bus.dto.Edge;
import com.mycom.navigation.bus.factory.BusInfra;

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
	
	public List<String> navigate(double sx, double sy, double ex, double ey) {
		Set<BusStop> startStaions = bif.arrounStops(sx, sy);
		Set<BusStop> endStops = bif.arrounStops(ex, ey);
		
		BusStop[] bsArr = bif.stopArray();
		
		boolean[] v = new boolean[bsArr.length];
		int[] v_cost = new int[v.length]; // 비용, 직전위치
		BusStop[] v_before = new BusStop[v.length];
		
		PriorityQueue<Dijk> pq = new PriorityQueue<Dijk>();
		for(BusStop bs : startStaions) {
			pq.offer(new Dijk(0,bs,null));
		}
		
		BusStop endpoint = null;
		
		while(!pq.isEmpty()){
			Dijk d = pq.poll();
			BusStop curr = d.curr;
			BusStop before = d.before;
			int currIdx = curr.getIdx();
			if(v[currIdx])continue;

			v[currIdx]=true;
			v_cost[currIdx] = d.cost ;
			v_before[currIdx] = before ;
			if(endStops.contains(curr)) {
				endpoint = curr;
				break;
			}
			// 버스로 정류장 이동.
			if(curr.getNextStops()!=null) {
				
				for(Entry<BusStop,Edge> ent : curr.getNextEntry()) {
					BusStop nextBs = ent.getKey();
					Edge e = ent.getValue();
					int value = e.getCost();
					
					//방문 기록 없는 정류장
					if(!v[nextBs.getIdx()]) {
						pq.offer(new Dijk(d.cost+value, nextBs, curr));
					}
				}
			}
			// 걸어서 정류장 이동
			int r = curr.getSectionRow();
			int c = curr.getSectionCol();
			for(int i=0; i<=8; i++) {
				int mr = r + dr[i];
				int mc = c + dc[i];
				Set<BusStop> stops = bif.stopsInSection(mr, mc);
				if(stops != null) {
					for(BusStop b : stops) {
						if(b.unusedStop())continue; // 경유 또는 가상 정류장
						if(!v[b.getIdx()]) {
							// 정류장 외 환승 +6
							pq.offer(new Dijk(d.cost+3, curr, b));
						}
					}
				}
			}
		}
		
		if(endpoint == null) return null;
		
		List<BusStop> list = new ArrayList<BusStop>();
		int idx = endpoint.getIdx();
		while(true) {
			list.add(bsArr[idx]);
			if(v_before[idx]==null)break;
			idx = v_before[idx].getIdx();
		}
		
		List<String> re = new ArrayList<String>();
		for(int i=list.size()-1; i>0; i--) {
			BusStop pre = list.get(i);
			BusStop next = list.get(i-1);
			String realPath = pre.findRealpath(next);
			re.add(realPath);
		}
		
		
		return re;
	}
	class Dijk implements Comparable<Dijk>{
		int cost;
		BusStop curr;
		BusStop before;
		
		public Dijk(int cost, BusStop curr, BusStop before) {
			this.cost = cost;
			this.curr = curr;
			this.before = before;
		}

		@Override
		public int compareTo(Dijk o) {
			return this.cost-o.cost;
		}

	}
	
}
