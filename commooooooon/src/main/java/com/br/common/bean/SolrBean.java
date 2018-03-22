package com.br.common.bean;

import java.util.Set;

public class SolrBean implements Comparable<SolrBean> {
	private String cell;
	private Set<String> ltime;
	private Set<String> realname;
	private Set<String> phone;
	private Set<String> location;

	/**
	 * 新地址增加省市区逻辑
	 * @return
	 */
	private String province;
	private String city;
	private String county;
	private String lstime; 
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getLstime() {
		return lstime;
	}
	public void setLstime(String lstime) {
		this.lstime = lstime;
	}
	public Set<String> getLtime() {
		return ltime;
	}
	public void setLtime(Set<String> ltime) {
		this.ltime = ltime;
	}
	public Set<String> getRealname() {
		return realname;
	}
	public void setRealname(Set<String> realname) {
		this.realname = realname;
	}
	public Set<String> getPhone() {
		return phone;
	}
	public void setPhone(Set<String> phone) {
		this.phone = phone;
	}
	public Set<String> getLocation() {
		return location;
	}
	public void setLocation(Set<String> location) {
		this.location = location;
	}
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	@Override
	public int compareTo(SolrBean solrbean) {
		if(this.lstime.compareTo(solrbean.getLstime()) > 0){
			return -1;
		}else if(this.lstime.compareTo(solrbean.getLstime()) < 0){
			return 1;
		}else{
			return 0;
		}
	}
	@Override
	public String toString() {
		return "SolrBean [cell=" + cell + ", ltime=" + ltime + ", realname="
				+ realname + ", phone=" + phone + ", location=" + location
				+ ", province=" + province + ", city=" + city + ", county="
				+ county + ", lstime=" + lstime + "]";
	}
	
}
