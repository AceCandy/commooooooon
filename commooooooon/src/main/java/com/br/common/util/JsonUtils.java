package com.br.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class JsonUtils {

	/**
	 * "$"作为子字段标识的第一个符号 US Unit Separator (单元分隔符) 用于分割如子字段
	 */
	public static final char F_CHAR31 = 31;

	/**
	 * 从json中读取tagPath处的值 tagPath用 :分隔 ，同个节点中取多个值用,分割 例：parent:name,id
	 * 是取parent节点下的 name，id 尽量使用 readValueFromJson(JsonNode json, String
	 * tagPath)
	 * 
	 * @param json
	 * @param tagPath
	 * @return List<String> 如果多个字段用 F_CHAR31 分割 , 如果为空，或者节点不存在返回 " "
	 *         返回list的长度一般与父节点个数相等，处理此返回值必须自行判断 "" ， " " 等问题
	 * @throws Exception
	 */
	public static List<String> readValueFromJson(String json, String tagPath) {
		// 返回值
		List<String> value = new ArrayList<String>();
		if (isEmpty(json) || (isEmpty(tagPath))) {
			return value;
		}
		ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
		String[] path = tagPath.split(":");
		JsonNode node = null;
		try {
			node = mapper.readTree(json);
		} catch (Exception e) {
			System.out.println("转换json异常，json：" + json + e.getMessage());
		}
		getJsonValue(node, path, value, 1);
		return value;
	}

	public static List<String> readValueFromJson(JsonNode json, String tagPath)throws Exception {
		// 返回值
		List<String> value = new ArrayList<String>();
		if (isEmpty(json) || (isEmpty(tagPath))) {
			return value;
		}
		String[] path = tagPath.split(":");
		getJsonValue(json, path, value, 1);
		return value;
	}

	public static void getJsonValue(JsonNode node, String[] path, List<String> values, int nextIndex) {
		if (isEmpty(node)) {
			return;
		}
		// 是路径的最后就直接取值
		if (nextIndex == path.length) {
			String endChild = path[nextIndex - 1];
			String[] ecs = StringUtils.split(endChild, ",");
			if (node.isArray()) {
				for (int i = 0; i < node.size(); i++) {
					setValues(ecs, node.get(i), values);
				}
			} else {
				setValues(ecs, node, values);
			}
			return;
		}
		// 判断是Node下是集合还是一个节点
		node = node.get(path[nextIndex - 1]);
		if (isEmpty(node)) {
			return;
		}
		if (node.isArray()) {
			for (int i = 0; i < node.size(); i++) {
				getJsonValue(node.get(i), path, values, nextIndex + 1);
			}
		} else {
			getJsonValue(node, path, values, nextIndex + 1);
		}
	}

	public static void setValues(String[] ecs, JsonNode node, List<String> values){
		ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
		StringBuffer vls = new StringBuffer();
		JsonNode node1 = null;
		if(node instanceof ObjectNode){
			node1 = (ObjectNode) node;
		}else if(node instanceof ArrayNode){
			for(int i=0;i<node.size();i++){
				for (int j = 0; j < ecs.length; j++) {
					JsonNode child = node.get(i).get(ecs[j]);
					if (j > 0) {
						vls.append(F_CHAR31);
					}
					vls.append(getNodeValue(child));
				}
			}
		}else{
			try {
				node1 = mapper.readTree(node.asText());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(node1 != null){
			for (int j = 0; j < ecs.length; j++) {
				JsonNode child = null;
				if(node1 instanceof ArrayNode){
					ArrayNode arrayNode = (ArrayNode) node1;
					for(int i=0;i<arrayNode.size();i++){
						child = arrayNode.get(i).get(ecs[j]);
						vls.append(getNodeValue(child));
					}
				}else{
					child = node1.get(ecs[j]);
					if (j > 0) {
						vls.append(F_CHAR31);
					}
					vls.append(getNodeValue(child));
				}
			}
		}
		values.add(vls.toString());
	}

	/**
	 * 向json中tagPath处的添加值 json用
	 * @param jsonData
	 * @param json
	 * @param tagPath
	 * @return
	 * @throws Exception
	 */
	public static void putValueFromJson(ObjectNode jsonData, Object json, String tagPath) throws Exception {
		// 返回值
		if (isEmpty(json) || (isEmpty(tagPath))) {
			return;
		}
		String[] path = tagPath.split(":");
		putJsonValue(json, path, jsonData);
		return;
	}

	public static void putJsonValue(Object node, String[] path, ObjectNode jsonData) throws JsonProcessingException, IOException {
		ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
		if (isEmpty(node) || isEmpty(path)) {
			return;
		}
		if(path.length == 1){
			setValuesTo(path[0],jsonData,node);
		}
		if(path.length == 2){
			JsonNode jsonNode = jsonData.get(path[0]);
			if(jsonNode != null){
				if(jsonNode instanceof ObjectNode){
					setValuesTo(path[1],(ObjectNode) jsonNode,node);
				}else if(jsonNode instanceof ArrayNode){
					ArrayNode arryNode = (ArrayNode) jsonNode;
					for(int i=0;i<arryNode.size();i++){
						ObjectNode jsonNode2 = (ObjectNode) arryNode.get(i);
						setValuesTo(path[1],jsonNode2,node);
					}
				}else{
					JsonNode jso = mapper.readTree(jsonNode.asText());
					if(jso instanceof ArrayNode){
						ArrayNode arrayNode = (ArrayNode) jso;
						for(int i=0;i<arrayNode.size();i++){
							ObjectNode jsonNode2 = (ObjectNode) arrayNode.get(i);
							setValuesTo(path[1],jsonNode2,node);
						}
					}else{
						ObjectNode js = (ObjectNode) jso;
						ObjectNode setValuesTo = setValuesTo(path[1],js,node);
						jsonData.remove(path[0]);
						jsonData.put(path[0], setValuesTo.toString());
					}
				}
			}
		}
		if(path.length == 3){
			JsonNode jsonNode2 = jsonData.get(path[0]);
			if(jsonNode2 != null){
				JsonNode jsonNode = jsonNode2.get(path[1]);
				if(jsonNode == null){
					return;
				}
				if(jsonNode instanceof ObjectNode){
					setValuesTo(path[2],(ObjectNode) jsonNode,node);
				}else if(jsonNode instanceof ArrayNode){
					ArrayNode arryNode = (ArrayNode) jsonNode;
					for(int i=0;i<arryNode.size();i++){
						ObjectNode json2 = (ObjectNode) arryNode.get(i);
						setValuesTo(path[2],json2,node);
					}
				}else{
					JsonNode jso = mapper.readTree(jsonNode.asText());
					if(jso instanceof ArrayNode){
						ArrayNode arrayNode = (ArrayNode) jso;
						for(int i=0;i<arrayNode.size();i++){
							ObjectNode json2 = (ObjectNode) arrayNode.get(i);
							setValuesTo(path[2],json2,node);
						}
					}else{
						ObjectNode js = (ObjectNode) jso;
						ObjectNode setValuesTo = setValuesTo(path[2],js,node);
						ObjectNode requestJson = (ObjectNode) jsonData.get(path[0]);
						requestJson.remove(path[1]);
						requestJson.put(path[1], setValuesTo.toString());
					}
				}
			}
		}
	}

	private static ObjectNode setValuesTo(String ecs, ObjectNode jsonNode, Object node) {
		if(node instanceof ArrayNode){
			ArrayNode node1 = (ArrayNode)node;
			jsonNode.put(ecs, node1);
		}else{
			String node1 = node.toString();
			jsonNode.put(ecs,node1);
		}
		return jsonNode;
	}

	public static void putValue(JsonNode jsonNode){
		
	}
	
	
	public static boolean isEmpty(Object obj) {
		boolean result = true;
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			result = (obj.toString().trim().length() == 0)
					|| obj.toString().trim().equals("null");
		} else if (obj instanceof Collection) {
			result = ((Collection<?>) obj).size() == 0;
		} else if (obj instanceof TextNode) {
			TextNode n = (TextNode) obj;
			return StringUtils.isBlank(n.textValue());
		} else {
			result = ((obj == null) || (obj.toString().trim().length() < 1)) ? true
					: false;
		}
		return result;
	}

	/**
	 * 获取值，如果为json，则直接toString
	 * 
	 * @param child
	 * @return 如果为空，或者节点不存在返回 " "
	 */
	public static Object getNodeValue(JsonNode child) {
		if (isEmpty(child))
			return "";
		if (child instanceof TextNode) {
			return child.textValue();
		} else {
			return child.toString();
		}
	}

	public static String getString(JsonNode child) {
		if (isEmpty(child))
			return "";
		if (child instanceof TextNode) {
			return child.textValue();
		} else {
			return child.toString();
		}
	}

	public static String getString(String nodeString, String key) {
		ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
		try {
			ObjectNode objectNode = (ObjectNode) mapper.readTree(nodeString);
			return getString(objectNode, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取String，如果为“NULL”,“null” 将返回""，如果值不为空将调用 trimToEmpty
	 * 
	 * @param child
	 * @param key
	 * @return
	 */
	public static String getString(ObjectNode child, String key) {
		if (isEmpty(child))
			return "";
		if (child.has(key)) {
			String vl = child.get(key).asText();
			if (StringUtils.isNotBlank(vl)) {
				return StringUtils.trimToEmpty(vl);
			}
			return "";
		} else {
			return "";
		}
	}

	public static List<String> getStringListByName(ObjectNode objectNode,
			String name) {
		List<String> rtn = new ArrayList<String>();
		if (objectNode.has(name)) {
			JsonNode j = objectNode.get(name);
			if (j.isArray()) {
				for (JsonNode jsonNode : j) {
					rtn.add(jsonNode.asText());
				}
			}
		}
		return rtn;
	}

	/**
	 * 获取String，如果为“NULL”,“null” 将返回"",如果值不为空将调用 trimToEmpty
	 * 
	 * @param child
	 * @param key
	 * @return
	 */
	public static String getString(JsonNode child, String key) {
		if (isEmpty(child))
			return "";
		if (child.has(key)) {
			String vl = child.get(key).asText();
			if (StringUtils.isNotBlank(vl)) {
				return StringUtils.trimToEmpty(vl);
			}
			return "";
		} else {
			return "";
		}
	}

	public static boolean isJson(String result) {
		if ((result.startsWith("{") && result.endsWith("}"))
				|| ((result.startsWith("[") && result.endsWith("]")))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * flag标签赋值
	 * 
	 * @param obj
	 *            flag ObjectNode对象
	 * @param mealName
	 *            套餐名字
	 * @param flag
	 *            flag值
	 */
	public static void setStatus(ObjectNode obj, String mealName, String flag) {
		obj.put(mealName, flag);
	}

	/**
	 * 判断jsonObject是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isJsonObjectEmpty(String str) {
		boolean result = false;
		if (str.startsWith("{") && str.endsWith("}")) {
			str = str.replace("{", "").replace("}", "").trim();
			if (str.length() == 0) {
				result = true;
			}
		}
		return result;
	}

	public static boolean isJsonObjectEmpty(ObjectNode objectNode) {
		if (!isEmpty(objectNode)) {
			String str = objectNode.toString();
			return isJsonObjectEmpty(str);
		}
		return true;
	}

}
