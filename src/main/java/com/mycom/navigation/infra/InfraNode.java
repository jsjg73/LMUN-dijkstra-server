
package com.mycom.navigation.infra;

import java.util.Iterator;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class InfraNode {
	private int idx;
	private String nodeId;
	private boolean disable;
	private Map<InfraNode, InfraEdge> nexts;
	
	public int getIdx() {
		return idx;
	}

	public Iterator<Map.Entry<InfraNode, InfraEdge>> nextsIterator() {
		if(nexts == null)return null;
		return nexts.entrySet().iterator();
	}

	public String getRealPathTo(InfraNode next) {
		return nexts.get(next).getRealPath();
	};
	
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
