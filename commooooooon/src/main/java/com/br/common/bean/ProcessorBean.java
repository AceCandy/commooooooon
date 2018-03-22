package com.br.common.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.br.common.util.JsonProcessUtil;
import com.br.common.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * <p>Description: 数据处理PO</p>
 * 
 * @Company: 百融（北京）金融信息服务股份有限公司
 * @Author: hongliang.zuo
 * @Date: 2016年9月22日
 * @version: 1.0
 */
public class ProcessorBean {
	protected String swift_number;
	
	private Map<String,MatchKeyBean> _matchKeyBeanMap = new HashMap<String,MatchKeyBean>();
	
	/**存放es地址信息*/
	protected Map<String,List<SolrBean>> solrResult = new ConcurrentHashMap<String,List<SolrBean>>();
	
	/**存放行为数据*/
	protected Map<String,HashMap<String, List<JsonNode>>> ecResult = new ConcurrentHashMap<String,HashMap<String, List<JsonNode>>>();
	
	/**存放航旅数据*/
	protected String hlData;
	
	/**存放QQgroup数据	 */
	protected Map<String,Object> qqGroupMap=new ConcurrentHashMap<String, Object>();
	
	protected List<JsonNode> otherJsonList = null;
	// redis请求的结果
	protected Map<String, List<JsonNode>> jsonMap;
//	protected List<SolrBean> solrDocumentList;
//	// 评分接口用
//	protected List<ProcessorResult> listResult;
	protected String srCode;// 用于进行判定是不是请求ice服务

	//海纳接口返回数据 key 为标示 db,bfd,ldzh ...  value为真实值
	private Map<String,String> dacRs = new HashMap<String,String>();
	
	//存放处理后的hbase返回结果,多次申请特殊名单使用过
	private Map<String,Map<String,String>> hbaseResult=new ConcurrentHashMap<String, Map<String,String>>();
	
	//存放多次申请hbase结果 
	private Map<String, Map<String, Map<String, String>>> applyHbaseResult=new ConcurrentHashMap<String, Map<String,Map<String,String>>>();
	
	
	public Map<String, Map<String, Map<String, String>>> getApplyHbaseResult() {
		return applyHbaseResult;
	}

	public void setApplyHbaseResult(
			Map<String, Map<String, Map<String, String>>> applyHbaseResult) {
		this.applyHbaseResult = applyHbaseResult;
	}

	public Map<String, Map<String, String>> getHbaseResult() {
		return hbaseResult;
	}

	public void setHbaseResult(Map<String, Map<String, String>> hbaseResult) {
		this.hbaseResult = hbaseResult;
	}

	// 新NEO4j
	protected volatile NeoBean newNeo4j = null;
	
	//三级类目
	Map<String,List<JsonNode>> ec3List = new ConcurrentHashMap<String,List<JsonNode>>();
	
	/**保存请求相关参数*/
	private ProcessorParam processorParam;
	
	public String getSrCode() {
		return srCode;
	}

	public void setSrCode(String srCode) {
		this.srCode = srCode;
	}

	public String getSwift_number() {
		return swift_number;
	}

	public void setSwift_number(String swift_number) {
		this.swift_number = swift_number;
	}
	/**
	 * 获取关系库
	 * @param flag 1、旧关系库  2、新关系库
	 * @return
	 */
	public MatchKeyBean getMatchKeyBean(String flag) {
		return this._matchKeyBeanMap.get(flag);
	}
	public void addMatchKeyBean(String flag,MatchKeyBean matchKeyBean){
		this._matchKeyBeanMap.put(flag, matchKeyBean);
	}
	
	/**
	 * neoFlag + subject
	 * @param neoFlag
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, List<JsonNode>> getEcResult(String neoFlag) throws Exception {
		return this.ecResult.get(neoFlag);
	}
	public Map<String,HashMap<String, List<JsonNode>>> getEcResultAll(){
		return this.ecResult;
	}
	public void setEcResult(Map<String,HashMap<String, List<JsonNode>>> ecResult) {
		this.ecResult = ecResult;
	}
	
	/**
	 * neoFlag + subject
	 * @param neoFlag
	 * @return
	 * @throws Exception
	 */
	public List<SolrBean> getSolrResult(String neoFlag) throws Exception {
		List<SolrBean> cur = this.solrResult.get(neoFlag);
		if(null == cur){
			cur = new ArrayList<SolrBean>();
		}
		return cur;
	}
	public Map<String,List<SolrBean>> getSolrResultAll(){
		return this.solrResult;
	}
	public void setSolrResult(Map<String,List<SolrBean>> solrResult) {
		this.solrResult = solrResult;
	}
	
	public ObjectNode getOnLineResult(String gid) {
		
		String result = getDacRs().get("bfd");
		if (StringUtils.isNotBlank(result)&& JsonUtils.isJson(result)) {
			ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
			try {
				return (ObjectNode) mapper.readTree(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Map<String, String> getDacRs() {
		return dacRs;
	}

	public void setDacRs(Map<String, String> dacRs) {
		this.dacRs = dacRs;
	}

	public void setNewNeo4j(NeoBean newNeo4j) {
		this.newNeo4j = newNeo4j;
	}
	public NeoBean getNewNeo4j(){
		return newNeo4j;

	}
	
	/**
	 * 根据neo版本获取三级类目数据  neoFlag格式为 neoFlag + subject
	 * @param neoFlag
	 * @return
	 */
	public List<JsonNode> getEc3List(String neoFlag) {
		return ec3List.get(neoFlag);
	}
	
	public Map<String,List<JsonNode>> getEc3ListAll() {
		return ec3List;
	}

	public void setEc3List(Map<String,List<JsonNode>> ec3List) {
		this.ec3List = ec3List;
	}

	public ProcessorParam getProcessorParam() {
		return processorParam;
	}

	public void setProcessorParam(ProcessorParam processorParam) {
		this.processorParam = processorParam;
	}

	public String getHlData() {
		return hlData;
	}

	public void setHlData(String hlData) {
		this.hlData = hlData;
	}

	public Map<String, Object> getQqGroupMap() {
		return qqGroupMap;
	}

	public void setQqGroupMap(Map<String, Object> qqGroupMap) {
		this.qqGroupMap = qqGroupMap;
	}

	
	
}
