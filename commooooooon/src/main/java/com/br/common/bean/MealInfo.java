package com.br.common.bean;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>
 * Description: 套餐相关信息
 * </p>
 * 
 * @Company: 百融（北京）金融信息服务股份有限公司
 * @Author: hongliang.zuo
 * @Date: 2016年9月22日
 * @version: 1.0
 */
public class MealInfo {

	private String id;

	private ServiceName serviceName;

	private String mealName = "";

	private String apiVersion = "";

	private String dataVersion = "";

	private String defaultValue = "";

	public MealInfo() {
		super();
	}

	public MealInfo(ServiceName serviceName, String mealName, String apiVersion, String dataVersion,
			String defaultValue) {
		super();
		this.serviceName = serviceName;
		this.mealName = mealName;
		this.apiVersion = apiVersion;
		this.dataVersion = dataVersion;
		this.defaultValue = defaultValue;
	}

	public MealInfo(String id, ServiceName serviceName, String mealName, String apiVersion, String dataVersion,
			String defaultValue) {
		super();
		this.id = id;
		this.serviceName = serviceName;
		this.mealName = mealName;
		this.apiVersion = apiVersion;
		this.dataVersion = dataVersion;
		this.defaultValue = defaultValue;
	}

	public MealInfo(ServiceName serviceName, String mealName, String apiVersion, String dataVersion) {
		super();
		this.serviceName = serviceName;
		this.mealName = mealName;
		this.apiVersion = apiVersion;
		this.dataVersion = dataVersion;
	}

	public String getDefaultValue() {
		return defaultValue==null?"":defaultValue;
	}

	/**
	 * 默认返回值，调用接口失败后，会降级为此值
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getApiVersion() {
		return apiVersion==null?"":apiVersion;
	}

	/**
	 * 套餐版本，api版本
	 */
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getDataVersion() {
		return dataVersion==null?"":dataVersion;
	}

	/**
	 * 套餐版本，数据版本
	 */
	public void setDataVersion(String dataVersion) {
		this.dataVersion = dataVersion;
	}

	public ServiceName getServiceName() {
		return serviceName;
	}

	public void setServiceName(ServiceName serviceName) {
		this.serviceName = serviceName;
	}

	public String getMealName() {
		return mealName;
	}

	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonIgnore
	@Override
	public int hashCode() {
		ServiceName sname = getServiceName();
		int sNameCode = 0;
		if(null != sname){
			sNameCode = sname.hashCode();
		}
		sNameCode = sNameCode+getMealName().hashCode()+getApiVersion().hashCode()+getDataVersion().hashCode();
		return sNameCode;
	}
	
	@JsonIgnore
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MealInfo other = (MealInfo) obj;
		if(StringUtils.equals(other.getServiceName().toString(), this.getServiceName().toString()) && StringUtils.equals(other.getMealName(), this.getMealName())
				&& StringUtils.equals(other.getApiVersion(), this.getApiVersion()) && StringUtils.equals(other.getDataVersion(), this.getDataVersion())){
			return true;
		}
		return true;
	}

	@Override
	public String toString() {
		return "MealInfo [id=" + id + ", serviceName=" + serviceName + ", mealName=" + mealName
				+ ", apiVersion=" + apiVersion + ", dataVersion=" + dataVersion + ", defaultValue="
				+ defaultValue + "]";
	}
}
