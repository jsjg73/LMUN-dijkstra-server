package com.mycom.navigation.infra;

import java.util.Iterator;
import java.util.Set;

public abstract class Infrastructure {
	public abstract Set<InfraNode> nodesNearby(double x, double y);
	public abstract Iterator<InfraNode> nodesNearby(InfraNode curr);
	public abstract InfraNode getNodeByIndex(int idx);
	public abstract InfraNode getNode(int idx);
	public abstract int nodesSize();
	
}
