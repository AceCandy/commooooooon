
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class Test {
	
	/**
	 * "$"作为子字段标识的第一个符号 US Unit Separator (单元分隔符) 用于分割如子字段
	 */
	public static final char F_CHAR31 = 31;
	
	/**  
	 * 从json中读取tagPath处的值 tagPath用 :分隔  ，同个节点中取多个值用,分割
	 * 例：parent:name,id  是取parent节点下的 name，id
	 * 尽量使用 readValueFromJson(JsonNode json, String tagPath)
	 * @param json  
	 * @param tagPath  
	 * @return  List<String>  如果多个字段用 F_CHAR31 分割 , 如果为空，或者节点不存在返回 " "
	 * 			返回list的长度一般与父节点个数相等，处理此返回值必须自行判断 "" ， " " 等问题 
	 * @throws Exception  
	 */  
	public static ObjectNode putValueFromJson(ObjectNode jsonData,Object json, String tagPath) throws Exception {   
	    // 返回值   
	    if (isEmpty(json) || (isEmpty(tagPath))) {   
	        return jsonData;   
	    }   
	    String[] path = tagPath.split(":");   
	    putJsonValue(json, path, jsonData, 1);   
	    return jsonData;   
	}
	public static void putJsonValue(Object node, String[] path, ObjectNode jsonData, int nextIndex){
		 if (isEmpty(node)) {   
		        return;   
		    }
		 // 是路径的最后就直接取值   
		 if (nextIndex == path.length) {
			 String endChild = path[nextIndex - 1];
		     String[] ecs = StringUtils.split(endChild,",");
		     if (jsonData.isArray()) {   
		            for (int i = 0; i < jsonData.size(); i++) {
		            	setValues(ecs,(ObjectNode)jsonData.get(i),node);
		            }   
		        } else {  
		        	setValues(ecs,jsonData,node);
		        }
		     return;
		 }
		 // 判断是Node下是集合还是一个节点   
		 jsonData = (ObjectNode) jsonData.get(path[nextIndex - 1]);  
	    if(isEmpty(jsonData)){
	    	 return;   
	    }
	    if (jsonData.isArray()) {   
	        for (int i = 0; i < jsonData.size(); i++) {   
	            putJsonValue(node, path, (ObjectNode) jsonData.get(i), nextIndex + 1);   
	        }   
	    } else {   
	    	putJsonValue(node, path, jsonData, nextIndex + 1);   
	    }   
	}

	private static void setValues(String[] ecs, ObjectNode jsonNode, Object node) {
		
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
			result = (obj.toString().trim().length() == 0) || obj.toString().trim().equals("null");
		} else if (obj instanceof Collection) {
			result = ((Collection<?>) obj).size() == 0;
		}else if (obj instanceof TextNode) {
			TextNode n = (TextNode)obj;
			return StringUtils.isBlank(n.textValue());
		}else {
			result = ((obj == null) || (obj.toString().trim().length() < 1)) ? true : false;
		}
		return result;
	}
	
	/**
	 * 获取值，如果为json，则直接toString
	 * @param child
	 * @return 如果为空，或者节点不存在返回 " "
	 */
	public static Object getNodeValue(JsonNode child){
		if(isEmpty(child))return "";
		if(child instanceof TextNode){
			return child.textValue();
		}else{
			return child.toString();
		}
	}
	
}
