package com.contactmanager.utils.viewutils;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.Vector;

public class TraversalPolicy extends FocusTraversalPolicy{
	Vector<Component> order;
	
	public TraversalPolicy(Vector<Component> order) {
		this.order = new Vector<Component>(order.size());
		this.order.addAll(order);
	}
	public Component getComponentAfter(Container focusCycleRoot,Component aComponent){
		int idx = (order.indexOf(aComponent) + 1) % order.size();
		if(order.get(idx).isEnabled()) {
			return order.get(idx);
		}
		else {
			return getComponentAfter(focusCycleRoot, order.get(idx));
		}		
	}
	
	
	public Component getComponentBefore(Container focusCycleRoot,Component aComponent){
		int idx = order.indexOf(aComponent) - 1;
		if (idx < 0) {
		  idx = order.size() - 1;
	}
		if(order.get(idx).isEnabled()) {
			return order.get(idx);
		}
		else {
			return getComponentBefore(focusCycleRoot, order.get(idx));
		}
	}
	
	public Component getDefaultComponent(Container focusCycleRoot) {
		return order.get(0);
	}
	
	public Component getLastComponent(Container focusCycleRoot) {
		return order.lastElement();
	}
	
	public Component getFirstComponent(Container focusCycleRoot) {
		return order.get(0);
	}
}
