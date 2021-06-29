package com.mycom.navigation.bus.navi;

import java.util.List;

import com.mycom.navigation.infra.InfraNode;
import com.mycom.navigation.infra.Infrastructure;

public abstract class Navigator {
	
	protected Infrastructure infrastructure;
	
	public Navigator(Infrastructure infrastructure) {
		this.infrastructure = infrastructure;
	}
	
	public abstract List<String> navigate(double sx, double sy, double ex, double ey);
	
	
	class Dijk implements Comparable<Dijk>{
		int cost;
		InfraNode curr;
		InfraNode pre;
		
		public Dijk(int cost, InfraNode curr, InfraNode pre) {
			this.cost = cost;
			this.curr = curr;
			this.pre = pre;
		}

		@Override
		public int compareTo(Dijk o) {
			return this.cost-o.cost;
		}

	}
}
