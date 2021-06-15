package com.mycom.navigation.bus.navi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import com.mycom.navigation.bus.dto.BusStation;
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
		Set<BusStation> startStaions = bif.arrounStations(sx, sy);
		Set<BusStation> endStations = bif.arrounStations(ex, ey);
		
		BusStation[] bsArr = bif.stationArray();
		
		boolean[] v = new boolean[bsArr.length];
		int[] v_cost = new int[v.length]; // 비용, 직전위치
		BusStation[] v_before = new BusStation[v.length];
		
		PriorityQueue<Dijk> pq = new PriorityQueue<Dijk>();
		for(BusStation bs : startStaions) {
			pq.offer(new Dijk(0,bs,null));
		}
		
		BusStation endpoint = null;
		
		while(!pq.isEmpty()){
			Dijk d = pq.poll();
			BusStation curr = d.curr;
			BusStation before = d.before;
			int currIdx = curr.getIdx();
			if(v[currIdx])continue;

			v[currIdx]=true;
			v_cost[currIdx] = d.cost ;
			v_before[currIdx] = before ;
			if(endStations.contains(curr)) {
				endpoint = curr;
				break;
			}
			// 버스로 정류장 이동.
			if(curr.getNext()!=null) {
				
				for(Entry<BusStation,Integer> ent : curr.getNextEntry()) {
					BusStation nextBs = ent.getKey();
					int value = ent.getValue();
					
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
				Set<BusStation> stations = bif.stationsInSection(mr, mc);
				if(stations != null) {
					for(BusStation b : stations) {
						if(b.unusedStation())continue; // 경유 또는 가상 정류장
						if(!v[b.getIdx()]) {
							// 정류장 외 환승 +6
							pq.offer(new Dijk(d.cost+3, curr, b));
						}
					}
				}
			}
		}
		
		if(endpoint == null) return null;
		
		List<BusStation> list = new ArrayList<BusStation>();
		int idx = endpoint.getIdx();
		while(true) {
			list.add(bsArr[idx]);
			if(v_before[idx]==null)break;
			idx = v_before[idx].getIdx();
		}
		
		List<String> re = new ArrayList<String>();
		for(int i=list.size()-1; i>0; i--) {
			BusStation from = list.get(i);
			BusStation to = list.get(i-1);
			re.add(from.getRealPathTbl().get(to.getNodeId()));
		}
		
		
		return re;
	}
	class Dijk implements Comparable<Dijk>{
		int cost;
		BusStation curr;
		BusStation before;
		
		public Dijk(int cost, BusStation curr, BusStation before) {
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
