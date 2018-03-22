package com.br.common.bean;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ProcessorResult {
	
	protected String code="";
	protected int level;
	protected int order;
	protected String key;
	protected ObjectNode node;
	protected ObjectNode nodeStatus;
	
	public ObjectNode getNodeStatus() {
		return nodeStatus;
	}
	public void setNodeStatus(ObjectNode nodeStatus) {
		this.nodeStatus = nodeStatus;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public ObjectNode getNode() {
		return node;
	}
	public void setNode(ObjectNode node) {
		this.node = node;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	
	
	
	
	
}
