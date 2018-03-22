package com.br.common.bean;

public class NeoResultBean {
	
	private String requestKeyType;
	private String neoKey;
	private String neoKeyType;
	private boolean isTarget = false;
	
	public String getRequestKeyType() {
		return requestKeyType;
	}
	public void setRequestKeyType(String requestKeyType) {
		this.requestKeyType = requestKeyType;
	}
	public String getNeoKey() {
		return neoKey;
	}
	public void setNeoKey(String neoKey) {
		this.neoKey = neoKey;
	}
	public String getNeoKeyType() {
		return neoKeyType;
	}
	public void setNeoKeyType(String neoKeyType) {
		this.neoKeyType = neoKeyType;
	}
	public boolean isTarget() {
		return isTarget;
	}
	public void setTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}
	
}
