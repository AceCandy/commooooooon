package com.br.common.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 加密jsonData中指定key
 * @author Alpha.wang
 * 2017年6月15日,下午4:12:06
 * BrCipherJsonUtils
 */
public class BrCipherJsonUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(BrCipherJsonUtils.class);
	
	/**
	 * path : 从jsonData中读取path处的值,如果有多个路径用","号分割,例子：requestParam:jsonData:id,requestParam:jsonData:cell或request_json:id,request_json_cell或
	 * 																   id,cell,mail,目前只支持这3中长度的path
	 * @param jsonData
	 * @param path
	 */
	public static String cipherJsonData(String jsonData,String path){
		ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
		if(StringUtils.isBlank(path) || !StringUtils.startsWith(jsonData, "{")){
			return jsonData;
		}
		if(StringUtils.isNotBlank(jsonData)){
			try {
				ObjectNode readTree = (ObjectNode) mapper.readTree(jsonData);
				String[] tagPaths = path.split(",");
				for(String tagPath:tagPaths){
					List<String> readValueFromJson = JsonUtils.readValueFromJson(readTree, tagPath);
					for(String value:readValueFromJson){
						if(StringUtils.isNotBlank(value)){
							ArrayNode arrayNode = mapper.createArrayNode();
							if(value.startsWith("[")&&value.endsWith("]")){
								logger.info("获取jsonData指定位置值:"+value);
								String value1 = value.substring(1, value.length()-1);
								if(StringUtils.isNotBlank(value1)){
									logger.info("截取jsonData后值 : " + value1);
									String[] v1s = value1.split(",");
									String encode="";
									for(String v:v1s){
										encode = BrCipherMaker.getInstance().encode(v.substring(1, v.length()-1));
										logger.info("加密后值 encode :" +encode);
										arrayNode.add(encode);
									}
									JsonUtils.putValueFromJson(readTree, arrayNode, tagPath);
								}
							}else{
								logger.info("jsonData值value : "+value);
								StringBuffer sb = new StringBuffer();
								String[] vs = value.split(",");
								String encode="";
								for(String va:vs){
									encode = BrCipherMaker.getInstance().encode(va);
									logger.info("加密后值encode : " + encode);
									sb.append(encode).append(",");
								}
								if(sb.length() > 0){
									JsonUtils.putValueFromJson(readTree, sb.substring(0, sb.length()-1), tagPath);
								}
							}
						}
					}
				}
				logger.info("加密后的jsonData 为 :" + readTree.toString());
				return readTree.toString();
			} catch (Exception e) {
				logger.error("特殊加密jsonData异常");
				e.printStackTrace();
			}
		}
		return null;
	}
	
//	public static void main(String[] args) {
//		String s="{\"cell\":[\"13840783486\"],\"id\":\"510102197203027028\",\"name\":\"薛颖\",\"ExtData\":{},\"meal\":\"Execution\"}";
//		String a = "{\"api_code\":\"1256561\",\"request_json\":\"{\\\"name\\\":\\\"孙琦\\\",\\\"id\\\":\\\"522522198402280021\\\",\\\"cell\\\":[\\\"13806583424\\\"],\\\"meal\\\":\\\"AccountChange_c,Consumption,scorebank\\\",\\\"cell_md5\\\":[\\\"c796f9176a3b428cf533d2ada7ab42eb\\\"]}\",\"request_ip\":\"10.20.2.138\",\"request_time\":1497577259679,\"msg_type\":\"request\",\"status\":1,\"swift_number\":\"1256561_20170616094059_7445\",\"response_time\":1497577262917,\"response_json\":\"{\\\"swift_number\\\":\\\"1256561_20170616094059_7445\\\",\\\"Flag\\\":{\\\"score\\\":\\\"99\\\",\\\"consumption\\\":\\\"1\\\",\\\"accountchange_c\\\":\\\"99\\\"},\\\"code\\\":\\\"00\\\",\\\"Score\\\":{}}\",\"t_cost\":3300,\"response_code\":\"00\"} ";
//		String b = "{\"requestParam\":{\"apiCode\":\"4000055\",\"swiftNumber\":\"4000055_20170615100936_2532\",\"jsonData\":{\"id\":\"532823198411031339\",\"cell\":[\"18908814149\",\"18656992610\"],\"mail\":[],\"name\":\"金永华\",\"user_date\":\"2017-06-15\",\"meal\":\"scorebank\",\"ifDeactivated\":0,\"sl_user_date\":\"2017-06-15\"}},\"meals\":[{\"id\":\"22\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Authentication\",\"apiVersion\":\"\",\"dataVersion\":\"E:location\",\"defaultValue\":\"authentication\"},{\"id\":\"24\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Media\",\"apiVersion\":\"\",\"dataVersion\":\"E:ecmedia\",\"defaultValue\":\"media\"},{\"id\":\"21\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Stability\",\"apiVersion\":\"\",\"dataVersion\":\"E:location\",\"defaultValue\":\"stability\"},{\"id\":\"38\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Assets\",\"apiVersion\":\"\",\"dataVersion\":\"E:ecmedia\",\"defaultValue\":\"assets\"},{\"id\":\"23\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Consumption\",\"apiVersion\":\"\",\"dataVersion\":\"E:ecmedia\",\"defaultValue\":\"consumption\"}]}";
//		String c = "{\"requestParam\":{\"apiCode\":\"4000055\",\"swiftNumber\":\"4000055_20170615100936_2532\",\"jsonData\":{\\\"id\":\\\"532823198411031339\\\",\\\"cell\\\":[\\\"18908814149\",\\\"18656992610\\\"],\\\"mail\\\":[],\\\"name\":\\\"金永华\\\",\\\"user_date\\\":\\\"2017-06-15\\\",\\\"meal\\\":\\\"scorebank\\\",\\\"ifDeactivated\\\":0,\\\"sl_user_date\\\":\\\"2017-06-15\\\"}}}";
//		System.out.println(BrCipherJsonUtils.cipherJsonData(c,"requestParam:jsonData:id,requestParam:jsonData:cell"));
////		System.out.println(BrCipherJsonUtils.cipherJsonData(a, "request_json:id,request_json:cell,request_json:mail"));
////		System.out.println(BrCipherJsonUtils.cipherJsonData(s, "id,cell"));
//	}
}
