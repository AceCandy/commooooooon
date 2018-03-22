import java.util.List;

import com.br.common.util.BrCipherMaker;
import com.br.common.util.JsonProcessUtil;
import com.br.common.util.JsonUtils;
import com.br.common.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class TestChiperJsonData {

	public static void main(String[] args) throws Exception {
		String str = "{\"requestParam\":{\"apiCode\":\"4000055\",\"swiftNumber\":\"4000055_20170615100936_2532\",\"jsonData\":{\"id\":\"532823198411031339\",\"cell\":[\"18908814149\",\"18656992610\"],\"mail\":[],\"name\":\"金永华\",\"user_date\":\"2017-06-15\",\"meal\":\"scorebank\",\"ifDeactivated\":0,\"sl_user_date\":\"2017-06-15\"}},\"meals\":[{\"id\":\"22\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Authentication\",\"apiVersion\":\"\",\"dataVersion\":\"E:location\",\"defaultValue\":\"authentication\"},{\"id\":\"24\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Media\",\"apiVersion\":\"\",\"dataVersion\":\"E:ecmedia\",\"defaultValue\":\"media\"},{\"id\":\"21\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Stability\",\"apiVersion\":\"\",\"dataVersion\":\"E:location\",\"defaultValue\":\"stability\"},{\"id\":\"38\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Assets\",\"apiVersion\":\"\",\"dataVersion\":\"E:ecmedia\",\"defaultValue\":\"assets\"},{\"id\":\"23\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Consumption\",\"apiVersion\":\"\",\"dataVersion\":\"E:ecmedia\",\"defaultValue\":\"consumption\"}]}";
		String str1="{\"requestParam\":{\"apiCode\":\"4000055\",\"swiftNumber\":\"4000055_20170615100936_2532\",\"jsonData\":{\"id\":\"532823198411031339\",\"cell\":[\"18908814149\",\"18656992610\"],\"mail\":2222,\"name\":\"金永华\",\"user_date\":\"2017-06-15\",\"meal\":\"scorebank\",\"ifDeactivated\":0,\"sl_user_date\":\"2017-06-15\"}},\"meals\":[{\"id\":\"22\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Authentication\",\"apiVersion\":\"\",\"dataVersion\":\"E:location\",\"defaultValue\":\"authentication\"},{\"id\":\"24\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Media\",\"apiVersion\":\"\",\"dataVersion\":\"E:ecmedia\",\"defaultValue\":\"media\"},{\"id\":\"21\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Stability\",\"apiVersion\":\"\",\"dataVersion\":\"E:location\",\"defaultValue\":\"stability\"},{\"id\":\"38\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Assets\",\"apiVersion\":\"\",\"dataVersion\":\"E:ecmedia\",\"defaultValue\":\"assets\"},{\"id\":\"23\",\"serviceName\":\"CORE_SERVICE\",\"mealName\":\"Consumption\",\"apiVersion\":\"\",\"dataVersion\":\"E:ecmedia\",\"defaultValue\":\"consumption\"}]}";
		String path = "requestParam:jsonData:id,requestParam:jsonData:cell";
		String[] tagPaths = path.split(",");
		ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
		ObjectNode readTree = (ObjectNode) mapper.readTree(str);
		for(String tagPath:tagPaths){
			List<String> readValueFromJson = JsonUtils.readValueFromJson(readTree, tagPath);
			System.out.println("{}{}{}:" + readValueFromJson);
			for(String value:readValueFromJson){
				if(StringUtils.isNotBlank(value)){
					if(value.startsWith("[")&&value.endsWith("]")){
						ArrayNode arrayNode = mapper.createArrayNode();
						String value1 = value.substring(1, value.length()-1);
						if(StringUtils.isNotBlank(value1)){
							String[] v1s = value1.split(",");
							String encode="";
							for(String v:v1s){
								System.out.println(v.substring(1, v.length()-1));
								encode = BrCipherMaker.getInstance().encode(v.substring(1, v.length()-1));
								arrayNode.add(encode);
							}
							System.out.println(arrayNode.toString());
							Test.putValueFromJson(readTree, arrayNode, tagPath);
						}
					}else{
						StringBuffer sb = new StringBuffer();
						String[] vs = value.split(",");
						String encode="";
						for(String va:vs){
							encode = BrCipherMaker.getInstance().encode(va);
							sb.append(encode).append(",");
						}
						if(sb.length() > 0){
							Test.putValueFromJson(readTree, sb.substring(0, sb.length()-1), tagPath);
						}
					}
				}
			}
		}
		System.out.println(readTree.toString());
	}
}
