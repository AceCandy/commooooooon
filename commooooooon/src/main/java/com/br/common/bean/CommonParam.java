package com.br.common.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.br.common.util.JsonProcessUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * <p>Description: 客户端参数处理类</p>
 * 
 * @Company: 百融（北京）金融信息服务股份有限公司
 * @Author: hongliang.zuo
 * @Date: 2016年9月22日
 * @version: 1.0
 */
public class CommonParam {
	private String apiCode;
	private String swiftNumber;
	private String jsonData = "{}";
	
	private ObjectNode _jsonData = JsonProcessUtil.getMapperInstance().createObjectNode();
	
	public String getSwiftNumber() {
		return swiftNumber;
	}
	public void setSwiftNumber(String swiftNumber) {
		this.swiftNumber = swiftNumber;
	}
	public String getApiCode() {
		return apiCode;
	}
	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}
	
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
		initJson();
	}
	
	public CommonParam(String apiCode, String swiftNumber, String jsonData) {
		super();
		this.apiCode = apiCode;
		this.swiftNumber = swiftNumber;
		this.jsonData = jsonData;
	}
	public CommonParam(){
		super();
	}
	private void initJson(){
		String json = getJsonData();
		if(StringUtils.isNotBlank(json)){
			ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
			try {
				this._jsonData = (ObjectNode)mapper.readTree(getJsonData());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@JsonIgnore
	public ObjectNode getInnerJsonData(){
		return this._jsonData;
	}
	
	/**
	 * 获取String字段值，不存在返回""
	 * @param name
	 * @return
	 */
	public String getStringByName(String name){
		String rtn = "";
		ObjectNode jd = this._jsonData;
		if(jd.has(name)){
			rtn = jd.get(name).asText();
		}
		return rtn;
	}
	
	/**
	 * 获取数组形式的参数，如cell
	 * @param name
	 * @return
	 */
	public List<String> getStringListByName(String name){
		List<String> rtn = new ArrayList<String>();
		ObjectNode jd = this._jsonData;
		if(jd.has(name)){
			JsonNode j = jd.get(name);
			if(j.isArray()){
				for (JsonNode jsonNode : j) {
					rtn.add(jsonNode.asText());
				}
			}
		}
		return rtn;
	}
	
	/**
	 * 获取ArrayNode(适用于linkCell)
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public ArrayNode getStringArrayNodeByName(String name) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper=JsonProcessUtil.getMapperInstance();
		ArrayNode arrayNode=mapper.createArrayNode();
		ObjectNode jd=this._jsonData;
		if(jd.has(name)){
			arrayNode=(ArrayNode) jd.get(name);
		}
		return arrayNode;
	}
	
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		String jsonData="";
		CommonParam commonParam=new CommonParam();
		commonParam.setApiCode("123");
		commonParam.setJsonData(jsonData);
		System.out.println(commonParam.getStringArrayNodeByName("linkman_cell").toString());
	}

}
