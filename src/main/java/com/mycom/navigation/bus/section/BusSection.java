package com.mycom.navigation.bus.section;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.mycom.navigation.bus.dto.BusStop;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class BusSection {
	private Set<BusStop>[][] sections;
	private XY minXY ;
	private XY maxXY ;
	private RC limitRC;
	public BusSection(int row, int col) {
		limitRC = new RC(row, col);
		maxXY = new XY(0,  0);
		minXY = new XY(999, 999);
	}
	
	private double XUnit;
	private double YUnit;
	public void initialization(Map<String, BusStop> busStopTbl) {
		if(busStopTbl == null) {
			throw new IllegalStateException("모든 버스 정류장 정보가 필요합니다.");
		}
		updateMinMaxXY(busStopTbl);
		int row = limitRC.getRow();
		int col = limitRC.getCol();
		XUnit = ((maxXY.getX() - minXY.getX())/row); // 0.0011009615529999905
		YUnit = ((maxXY.getY() - minXY.getY())/col); // 0.0016079761373999873

		sections = new HashSet[row+2][col+2];
		
		for(Map.Entry<String , BusStop> ent : busStopTbl.entrySet()) {
			BusStop bs = ent.getValue();
			int r = cvtX2R(bs.getX());
			int c = cvtY2C(bs.getY());
			if(sections[r][c] == null) {
				sections[r][c] = new HashSet<BusStop>();
			}
			sections[r][c].add(bs);
			bs.setSectionRow(r);
			bs.setSectionCol(c);
		}
	}
	private void updateMinMaxXY(Map<String, BusStop> busStopTbl) {
		for(Map.Entry<String , BusStop> ent : busStopTbl.entrySet()) {
			BusStop bs = ent.getValue();
			minMaxXY(bs);
		}
	}
	
	
	public Set<BusStop> arrounStops(double x, double y){
		
		boolean[][] v = new boolean[limitRC.getRow()+2][limitRC.getCol()+2];
		int r = cvtX2R(x);
		int c = cvtY2C(y);
		
		Queue<RC> que = new LinkedList<RC>();
		que.offer(new RC(r, c));
		
		int[] dr = {0,-1,-1,0,1,1,1,0,-1};
		int[] dc = {0,0,1,1,1,0,-1,-1,-1};
		
		while(!que.isEmpty()) {
			RC cur = que.poll();
			r = cur.getRow();
			c = cur.getCol();
			if(v[r][c])continue;
			v[r][c] =true;
			if(containsStop(r,c))break;
			for(int i=1; i<=8; i++) {
				int mr = r + dr[i];
				int mc = c + dc[i];
				if(posible(r,c,v) && !v[mr][mc]) {
					que.offer(new RC(mr,mc));
				}
			}
		}
		return sections[r][c];
	}
	private boolean posible(int r, int c, boolean[][] v) {
		return r>=0 && r < v.length && c>=0 && c<v[0].length;
	}
	public boolean containsStop(int r, int c) {
		Set<BusStop> section = sections[r][c];
		if(section == null)return false;
		for(BusStop bs : section) {
			if(bs.unusedStop())continue;
			return true;
		}
		return false;
	}
	
	public void minMaxXY(BusStop stop) {
		if(maxXY == null)maxXY = new XY(0,  0);
		if(minXY == null)minXY = new XY(999, 999);
		maxXY.max(stop.getX(), stop.getY());
		minXY.min(stop.getX(), stop.getY());
	}
	public int cvtX2R(double x) {
		return (int)( (x-minXY.getX())/XUnit );
	}
	public int cvtY2C(double y) {
		return (int)( (y-minXY.getY())/YUnit );
	}
	
	public Set<BusStop> stopsInSection(int r, int c){
		return sections[r][c];
	}
}
