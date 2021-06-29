package com.mycom.navigation.bus.navi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Map.Entry;

import com.mycom.navigation.bus.dto.BusStop;
import com.mycom.navigation.bus.dto.Edge;
import com.mycom.navigation.bus.navi.Navigation.Dijk;
import com.mycom.navigation.infra.InfraEdge;
import com.mycom.navigation.infra.InfraNode;
import com.mycom.navigation.infra.Infrastructure;

public class DijkstraNavigator extends Navigator {
	
	public DijkstraNavigator(Infrastructure infrastructure) {
		super(infrastructure);
	}
	
	@Override
	public List<String> navigate(double sx, double sy, double ex, double ey) {
		
		List<InfraNode> optimizedRoute = navigateByDijkstra(sx, sy, ex, ey);
		
		return realPathsBetweenNodes(optimizedRoute);
	}
	private List<InfraNode> navigateByDijkstra(double sx, double sy, double ex, double ey){
		
		Set<InfraNode> startNodes = nodesNearby(sx, sy);
		Set<InfraNode> endNodes = nodesNearby(ex, ey);
		
		return dijkstra(startNodes, endNodes);
	}

	private Set<InfraNode> nodesNearby(double x, double y){
		Set<InfraNode> nodes = infrastructure.nodesNearby(x, y);
		if(nodes.size() == 0)
			throw new IllegalArgumentException("there are no infraNodes near by "+x+", "+y);
		return nodes;
	}
	
	private List<InfraNode> dijkstra(Set<InfraNode> startNodes, Set<InfraNode> endNodes) {
		// TODO Auto-generated method stub
		int nodesSize = infrastructure.nodesSize();
		
		boolean[] visited = new boolean[nodesSize];
		InfraNode[] previousNode = new InfraNode[nodesSize];
		
		PriorityQueue<Dijk> pq = createPriorityQueue(startNodes);
		
		InfraNode endNode = null;
		while(!pq.isEmpty()) {
			Dijk d = pq.poll();
			InfraNode curr = d.curr;
			InfraNode previous = d.pre;
			int cost = d.cost;
			int currIdx = curr.getIdx();
			if(visited[currIdx])continue;

			visited[currIdx]=true;
			previousNode[currIdx] = previous ;
			if(endNodes.contains(curr)) {
				endNode = curr;
				break;
			}
			offerNodesNearbyCurr(pq, curr, cost, visited);
		}
		if(endNode == null) {
			throw new IllegalArgumentException("could not find appropricate route");
		}
		
		return traceBackNodes(endNode, previousNode);
	}

	private List<InfraNode> traceBackNodes(InfraNode endNode, InfraNode[] previousNode) {
		List<InfraNode> optimizedRoute = new ArrayList<InfraNode>();
		InfraNode curr = endNode;
		while(true) {
			optimizedRoute.add(curr);
			int idx = curr.getIdx();
			if(previousNode[idx]==null)break;
			curr = previousNode[idx];
		}
		return optimizedRoute;
	}

	private void offerNodesNearbyCurr(PriorityQueue<Dijk> pq, InfraNode curr, int cost, boolean[] visited) {
		
		Iterator<Entry<InfraNode, InfraEdge>> iter = curr.nextsIterator();
		if(iter==null)return;
		
		while(iter.hasNext()) {
			Entry<InfraNode, InfraEdge> entry = iter.next();
			InfraNode nextNode = entry.getKey();
			InfraEdge edge = entry.getValue();
			
			int nextCost = edge.getCost();
			int nextNodeIdx = nextNode.getIdx();
			if(! visited[nextNodeIdx]) {
				pq.offer(new Dijk(cost+nextCost, nextNode, curr));
			}
		}
	}

	private PriorityQueue<Dijk> createPriorityQueue(Set<InfraNode> startNodes) {
		PriorityQueue<Dijk> pq = new PriorityQueue<Dijk>();
		for(InfraNode node : startNodes) {
			pq.add(new Dijk(0, node, null));
		}
		return pq;
	}

	
	private List<String> realPathsBetweenNodes(List<InfraNode> optimizedRoute){
		List<String> realPaths = new ArrayList<String>();
		for(int i=optimizedRoute.size()-1; i>0; i--) {
			InfraNode pre = optimizedRoute.get(i);
			InfraNode next = optimizedRoute.get(i-1);
			String realPath = pre.getRealPathTo(next);
			realPaths.add(realPath);
		}
		
		return realPaths;
	}

}
