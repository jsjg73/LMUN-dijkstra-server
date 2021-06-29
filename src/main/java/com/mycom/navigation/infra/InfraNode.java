
package com.mycom.navigation.infra;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class InfraNode {
	protected int idx;
	protected String nodeId;
	protected Map<InfraNode, InfraEdge> nexts;
	protected boolean enable = true;
	protected double x;
	protected double y;
	public InfraNode(int idx, String nodeId) {
		super();
		this.idx = idx;
		this.nodeId = nodeId;
	}
	
	public void addNextNode(InfraNode node, int cost, String realPath) {
		if(node == null)return;
		if(nexts == null)nexts = new HashMap<InfraNode, InfraEdge>();
		InfraEdge edge = nexts.get(node);
		if(edge == null) {
			edge = new InfraEdge(cost, realPath);
			nexts.put(node, edge);
		}else {
			if(edge.getCost() > cost) {
				edge.setCost(cost);
				edge.setRealPath(realPath);
			}
		}
	}
	
	public String findRealpath(InfraNode next) {
		return nexts.get(next).getRealPath();
	}
	public Iterator<Map.Entry<InfraNode, InfraEdge>> nextsIterator() {
		if(nexts == null)return null;
		return nexts.entrySet().iterator();
	}

	public String getRealPathTo(InfraNode next) {
		return nexts.get(next).getRealPath();
	};
	public boolean connected(InfraNode node) {
		return nexts != null && nexts.containsKey(node);
	}
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null  || getClass() != obj.getClass())return false;
		
		InfraNode e = (InfraNode)obj;
		
		return nodeId.equals(e.getNodeId());
	}
	
	@Override
    public int hashCode() {
		return nodeId.hashCode();
    }

}
