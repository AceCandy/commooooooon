package com.br.common.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * <p>
 * Description: 用于调用服务之间参数传递
 * </p>
 * 
 * @Company: 百融（北京）金融信息服务股份有限公司
 * @Author: hongliang.zuo
 * @Date: 2016年9月22日
 * @version: 1.0
 */
@JsonInclude(Include.NON_EMPTY) 
public class ProcessorParam {

	public ProcessorParam() {
		super();
	}

	private CommonParam requestParam = new CommonParam();
	private Set<MealInfo> meals = new HashSet<MealInfo>();

	private Map<String, Object> _innerMap = new ConcurrentHashMap<String, Object>();

	// 请求的套餐
	private Map<ServiceName, HashSet<MealInfo>> set_request = new HashMap<ServiceName, HashSet<MealInfo>>();

	private Map<ServiceName, HashSet<MealInfo>> set_rule_depends = new HashMap<ServiceName, HashSet<MealInfo>>();
	
	private Map<ServiceName, HashSet<MealInfo>> set_tree_depends = new HashMap<ServiceName, HashSet<MealInfo>>();

	private Map<ServiceName, HashSet<MealInfo>> set_score_depends = new HashMap<ServiceName, HashSet<MealInfo>>();

	public CommonParam getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(CommonParam requestParam) {
		this.requestParam = requestParam;
	}

	/**
	 * 添加临时数据
	 * 
	 * @param key
	 * @param value
	 */

	public void addTemp(String key, Object value) {
		this._innerMap.put(key, value);
	}

	/**
	 * 获取临时数据
	 * 
	 * @param key
	 * @return
	 */
	public Object getTemp(String key) {
		return this._innerMap.get(key);
	}

	/**
	 * 基础验证是否通过，此验证条件为id、cell、mail同时为空返回false，否则true
	 * 
	 * @return
	 */
	public boolean baseValidatePass() {
		// id,mail,cell至少有一个
		if (StringUtils.isNotBlank(requestParam.getStringByName("id")) || requestParam.getStringListByName("mail").size() > 0 || requestParam.getStringListByName("cell").size() > 0) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public Map<ServiceName, HashSet<MealInfo>> getSet_request() {
		return set_request;
	}

	public void setSet_request(Map<ServiceName, HashSet<MealInfo>> set_request) {
		this.set_request = set_request;
	}

	@JsonIgnore
	public Map<ServiceName, HashSet<MealInfo>> getSet_rule_depends() {
		return set_rule_depends;
	}

	public void setSet_rule_depends(Map<ServiceName, HashSet<MealInfo>> set_rule_depends) {
		this.set_rule_depends = set_rule_depends;
	}

	@JsonIgnore
	public Map<ServiceName, HashSet<MealInfo>> getSet_score_depends() {
		return set_score_depends;
	}

	public void setSet_score_depends(Map<ServiceName, HashSet<MealInfo>> set_score_depends) {
		this.set_score_depends = set_score_depends;
	}

	public Set<MealInfo> getMeals() {
		return meals;
	}

	public void setMeals(Set<MealInfo> meals) {
		this.meals = meals;
	}

	@JsonIgnore
	public List<MealInfo> getMealList() {
		List<MealInfo> mealList = new ArrayList<MealInfo>();
		Iterator<MealInfo> it = this.meals.iterator();
		while (it.hasNext()) {
			mealList.add(it.next());
		}
		return mealList;
	}

	public Map<String, Object> get_innerMap() {
		return _innerMap;
	}

	@JsonIgnore
	public Map<ServiceName, HashSet<MealInfo>> getSet_tree_depends() {
		return set_tree_depends;
	}

	public void setSet_tree_depends(Map<ServiceName, HashSet<MealInfo>> set_tree_depends) {
		this.set_tree_depends = set_tree_depends;
	}

}
