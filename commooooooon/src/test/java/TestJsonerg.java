import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.br.common.util.JsonProcessUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;


public class TestJsonerg {
	
	public static void main(String[] args) throws JsonProcessingException, IOException {
		ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
		String json = "[{\"id\":\"NDgxCAcENDo8BTEBNA0Α5JATFd\",\"name\":\"李洪伟\",\"cell\":[\"MxA6CwΑ1IyPj0CMzQ\"],\"meal\":\"SpecialList_c\",\"ifDeactivated\":0,\"user_date\":\"2017-06-22\",\"sl_user_date\":\"2017-06-22\"},{\"id\":\"NDgxCAcENDo8BTEBNA0Α5JATFd\",\"name\":\"李洪伟\",\"cell\":[\"MxA6CwΑ1IyPj0CMzQ\"],\"meal\":\"SpecialList_c\",\"ifDeactivated\":0,\"user_date\":\"2017-06-22\",\"sl_user_date\":\"2017-06-22\"}]";
		ArrayNode readTree = (ArrayNode) mapper.readTree(json);
		RequestParam commonParam = new RequestParam();
		CommonParam com = new CommonParam();
		commonParam.setApiCode("1256561");
		commonParam.setSwiftNumber("1256561_20170622134850_3426");
		commonParam.setJsonData(readTree.toString());
		com.setRequestParam(commonParam);
		String paramStr = mapper.writeValueAsString(com);
		String jsonDa = "{\"api_code\":\"1256561\",\"request_json\":\"{\\\"name\\\":\\\"孙琦\\\",\\\"id\\\":\\\"522522198402280021\\\",\\\"cell\\\":[\\\"13806583424\\\"],\\\"meal\\\":\\\"AccountChange_c,Consumption,scorebank\\\",\\\"cell_md5\\\":[\\\"c796f9176a3b428cf533d2ada7ab42eb\\\"]}\",\"request_ip\":\"10.20.2.138\",\"request_time\":1497577259679,\"msg_type\":\"request\",\"status\":1,\"swift_number\":\"1256561_20170616094059_7445\",\"response_time\":1497577262917,\"response_json\":\"{\\\"swift_number\\\":\\\"1256561_20170616094059_7445\\\",\\\"Flag\\\":{\\\"score\\\":\\\"99\\\",\\\"consumption\\\":\\\"1\\\",\\\"accountchange_c\\\":\\\"99\\\"},\\\"code\\\":\\\"00\\\",\\\"Score\\\":{}}\",\"t_cost\":3300,\"response_code\":\"00\"}";
		String jsonDa1="{\"requestParam\":{\"apiCode\":\"4000055\",\"swiftNumber\":\"4000055_20170615100936_2532\",\"jsonData\":{\"id\":\"532823198411031339\",\"cell\":[\"18908814149\",\"18656992610\"],\"mail\":[],\"name\":\"金永华\",\"user_date\":\"2017-06-15\",\"meal\":\"scorebank\",\"ifDeactivated\":0,\"sl_user_date\":\"2017-06-15\"}},\"meals\":[{\"id\":\"22\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Authentication\",\"apiVersion\":\"\",\"dataVersion\":\"E:location\",\"defaultValue\":\"authentication\"},{\"id\":\"24\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Media\",\"apiVersion\":\"\",\"dataVersion\":\"E:ecmedia\",\"defaultValue\":\"media\"},{\"id\":\"21\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Stability\",\"apiVersion\":\"\",\"dataVersion\":\"E:location\",\"defaultValue\":\"stability\"},{\"id\":\"38\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Assets\",\"apiVersion\":\"\",\"dataVersion\":\"E:ecmedia\",\"defaultValue\":\"assets\"},{\"id\":\"23\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Consumption\",\"apiVersion\":\"\",\"dataVersion\":\"E:ecmedia\",\"defaultValue\":\"consumption\"}]}";
		String jsonDa2="{\"requestParam\":{\"apiCode\":\"4000055\",\"swiftNumber\":\"4000055_20170615100936_2532\",\"jsonData\":{\\\"id\\\":\\\"zzzzzzzzzzzzzzzzzzzzz\\\",\\\"cell\\\":[\\\"18908814149\\\",\\\"18656992610\\\"],\\\"mail\\\":[],\\\"name\\\":\\\"金永华\\\",\\\"user_date\\\":\\\"2017-06-15\\\",\\\"meal\\\":\\\"scorebank\\\",\\\"ifDeactivated\\\":0,\\\"sl_user_date\\\":\\\"2017-06-15\\\"}}}";
		String json22 = "{\"requestParam\":{\"apiCode\":\"1256561\",\"swiftNumber\":\"1256561_20161212163022_2835\",\"jsonData\":\"{\\\"meal\\\":\\\"SpecialList_c\\\",\\\"id\\\":\\\"132425197604244906\\\",\\\"name\\\":\\\"陈赞群\\\",\\\"cell\\\":[\\\"13400276035\\\"],\\\"bank_id\\\":\\\"6228480388952276979\\\",\\\"ifDeactivated\\\":0,\\\"user_date\\\":\\\"\\\",\\\"user_time\\\":\\\"\\\"}\"},\"meals\":[{\"id\":\"100\",\"serviceName\":\"SL_SERVICE\",\"mealName\":\"SpecialList_c\",\"apiVersion\":\"\",\"dataVersion\":\"H:Black_List1\",\"defaultValue\":\"specialList_c\"}]}";
		String str = "{\"requestParam\":{\"apiCode\":\"1256561\",\"swiftNumber\":\"1256561_20170622134850_3426\",\"jsonData\":{\"id\":\"NDgxCAcENDo8BTEBNA0Α5JATFd\",\"name\":\"李洪伟\",\"cell\":[\"MxA6CwΑ1IyPj0CMzQ\"],\"meal\":\"SpecialList_c\",\"ifDeactivated\":0,\"user_date\":\"2017-06-22\",\"sl_user_date\":\"2017-06-22\"}},\"meals\":[{\"id\":\"8\",\"serviceName\":\"SL_SERVICE\",\"mealName\":\"SpecialList_c\",\"apiVersion\":\"\",\"dataVersion\":\"H:Black_List1\",\"defaultValue\":\"specialList_c\"}]}";
		String str2="{\"requestParam\":{\"apiCode\":\"1256561\",\"swiftNumber\":\"1256561_20170622134850_3426\",\"jsonData\":[{\"id\":\"NDgxCAcENDo8BTEBNA0Α5JATFd\",\"name\":\"李洪伟\",\"cell\":[\"MxA6CwΑ1IyPj0CMzQ\"],\"meal\":\"SpecialList_c\",\"ifDeactivated\":0,\"user_date\":\"2017-06-22\",\"sl_user_date\":\"2017-06-22\"},{\"id\":\"NDgxCAcENDo8BTEBNA0Α5JATFd\",\"name\":\"李洪伟\",\"cell\":[\"MxA6CwΑ1IyPj0CMzQ\"],\"meal\":\"SpecialList_c\",\"ifDeactivated\":0,\"user_date\":\"2017-06-22\",\"sl_user_date\":\"2017-06-22\"}]}}";
		ObjectNode jsonData = (ObjectNode) mapper.readTree(str);
		String path = "ABCDEFGHIGKLMNOPQRST";
		String tagPath="requestParam:jsonData:id";//request_json:id,requestParam:jsonData:id
		try {
			ObjectNode putValueFromJson = putValueFromJson(jsonData, path, tagPath);
			System.out.println(putValueFromJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ObjectNode putValueFromJson(ObjectNode jsonData, Object json, String tagPath) throws Exception {
		ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
		// 返回值
		if (isEmpty(json) || (isEmpty(tagPath))) {
			return jsonData;
		}
		String[] path = tagPath.split(":");
		Map<Integer, JsonNode> map = new TreeMap<Integer, JsonNode>();
		ObjectNode originalJson = mapper.createObjectNode();
		originalJson = jsonData;
		putJsonValue(json, path, jsonData, 1, map,mapper,originalJson);
		return originalJson;
	}

	public static void putJsonValue(Object node, String[] path, JsonNode jsonData, int nextIndex, Map<Integer, JsonNode> map, ObjectMapper mapper, ObjectNode originalJson) {
		if (isEmpty(node)) {
			return;
		}
		// 是路径的最后就直接取值
		if (nextIndex == path.length) {
			String endChild = path[nextIndex - 1];
			String[] ecs = StringUtils.split(endChild, ",");
			if (jsonData.isArray()) {
				for (int i = 0; i < jsonData.size(); i++) {
					setValuesTo(ecs, (ObjectNode) jsonData.get(i), node);
				}
			} else {
				setValuesTo(ecs, (ObjectNode) jsonData, node);
			}
			if(map.size()>0){
				for(Entry<Integer, JsonNode> entry : map.entrySet()){
					Integer key = entry.getKey();
					ObjectNode pathNode = (ObjectNode) entry.getValue();
					for(int i=0;i<=key;i++){
						if(i==key){
							originalJson.remove(path[key]);
							originalJson.put(path[key], pathNode.toString());
						}else{
							originalJson = (ObjectNode) originalJson.path(path[i]);
						}
					}
				}
			}
			return;
		}
		// 判断是Node下是集合还是一个节点
		JsonNode jsonNode = jsonData.get(path[nextIndex - 1]);
		if(jsonNode instanceof ObjectNode){
			jsonData = (ObjectNode) jsonNode;
		}else if(jsonNode instanceof ArrayNode){
			jsonData = jsonNode;
		}else{
			try {
				jsonData = mapper.readTree(jsonNode.asText());
				if(jsonData instanceof ObjectNode){
					jsonData = (ObjectNode)jsonData;
					map.put(nextIndex - 1, jsonData);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		if (isEmpty(jsonData)) {
			return;
		}
		if (jsonData.isArray()) {
			for (int i = 0; i < jsonData.size(); i++) {
				putJsonValue(node, path, (ObjectNode) jsonData.get(i), nextIndex + 1, map, mapper, originalJson);
			}
		} else {
			putJsonValue(node, path, jsonData, nextIndex + 1, map, mapper, originalJson);
		}
	}

	private static void setValuesTo(String[] ecs, ObjectNode jsonNode, Object node) {
		for (int j = 0; j < ecs.length; j++) {
			if(node instanceof ArrayNode){
				ArrayNode node1 = (ArrayNode)node;
				jsonNode.put(ecs[j], node1);
			}else{
				String node1 = node.toString();
				jsonNode.put(ecs[j], node1);
			}
		}
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
	
}
