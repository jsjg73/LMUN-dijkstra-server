package com.mycom.navigation.infra;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Infrastructure {
	protected Map<String, InfraNode> nodes = new HashMap<String, InfraNode>();
	protected int nodeSize=0;
	
	public Iterator<InfraNode> nodesIterator(){
		return nodes.values().iterator();
	}
	public InfraNode getNode(String nodeId) {
		return nodes.get(nodeId);
	}
	
	public void addNode(InfraNode node) {
		if(nodes.put(node.getNodeId(), node)==null)
			nodeSize++;
	}
	public int nodesSize() {
		return nodeSize;
	}
	public abstract Set<InfraNode> nodesNearby(double x, double y);
	public abstract InfraNode getNodeByIndex(int idx);
}
