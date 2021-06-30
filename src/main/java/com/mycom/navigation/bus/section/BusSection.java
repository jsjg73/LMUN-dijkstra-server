package com.mycom.navigation.bus.section;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.mycom.navigation.infra.InfraNode;
import com.mycom.navigation.infra.Infrastructure;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class BusSection {
	private final int ROW=500, COL=500;
	private Set<InfraNode>[][] sections;
	private XY minXY ;
	private XY maxXY ;
	public BusSection() {
		maxXY = new XY(0,  0);
		minXY = new XY(999, 999);
	}
	
	private double XUnit;
	private double YUnit;
	public void initialization(Infrastructure busInfra) {
		if(busInfra.getNodes() == null) {
			throw new IllegalStateException("모든 버스 정류장 정보가 필요합니다.");
		}
		updateMinMaxXY(busInfra);
		XUnit = ((maxXY.getX() - minXY.getX())/ROW); // 0.0011009615529999905
		YUnit = ((maxXY.getY() - minXY.getY())/COL); // 0.0016079761373999873

		sections = new HashSet[ROW+2][COL+2];
		Iterator<InfraNode> iter = busInfra.nodesIterator();
		while(iter.hasNext()) {
			InfraNode node = iter.next();
			int r = cvtX2R(node.getX());
			int c = cvtY2C(node.getY());
			if(sections[r][c] == null) {
				sections[r][c] = new HashSet<InfraNode>();
			}
			sections[r][c].add(node);
		}
	}
	private void updateMinMaxXY(Infrastructure busInfra) {
		Iterator<InfraNode> iter = busInfra.nodesIterator();
		while(iter.hasNext()) {
			minMaxXY(iter.next());
		}
	}
	
	// 입력 좌표 주변 9 구역에 있는 정류장 집합 리턴
	public Set<InfraNode> nodesNearby(InfraNode curr){
		return nodesNearby(curr.getX(), curr.getY());
	}
	
	public Set<InfraNode> nodesNearby(double x, double y){
		int r = cvtX2R(x);
		int c = cvtY2C(y);
		
		int[] dr = {0,-1,-1,0,1,1,1,0,-1};
		int[] dc = {0,0,1,1,1,0,-1,-1,-1};
		
		Set<InfraNode> unioned = new HashSet<InfraNode>();
		for(int i=1; i<=8; i++) {
			int mr = r + dr[i];
			int mc = c + dc[i];
			if(posible(mr, mc) && sections[mr][mc] != null) {
				unionEnabled(unioned, sections[mr][mc]);
			}
		}
		
		return unioned;
	}
	
	private boolean posible(int r, int c) {
		return r>=0 && r < ROW && c>=0 && c< COL;
	}
	private void unionEnabled(Set<InfraNode> big, Set<InfraNode> small){
		for(InfraNode node : small) {
			if(node.isEnable())
				big.add(node);
		}
	}
	
	public void minMaxXY(InfraNode infraNode) {
		if(maxXY == null)maxXY = new XY(0,  0);
		if(minXY == null)minXY = new XY(999, 999);
		maxXY.max(infraNode.getX(), infraNode.getY());
		minXY.min(infraNode.getX(), infraNode.getY());
	}
	public int cvtX2R(double x) {
		return (int)( (x-minXY.getX())/XUnit );
	}
	public int cvtY2C(double y) {
		return (int)( (y-minXY.getY())/YUnit );
	}
	
}
