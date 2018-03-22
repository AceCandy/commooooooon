import java.util.Collection;

import com.br.common.util.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;


public class TestJsonFor {

	
	public static void changeJsonValue(ObjectNode pathNode,JsonNode originalJson,String[] path,String repalceNode,int nextIndex){
		
		if(StringUtils.equals(repalceNode, path[nextIndex])){
			originalJson = pathNode;
		}
		// 判断是Node下是集合还是一个节点
		originalJson = originalJson.get(path[nextIndex - 1]);
		if (isEmpty(originalJson)) {
			return;
		}
		if (originalJson.isArray()) {
			for (int i = 0; i < originalJson.size(); i++) {
				changeJsonValue(pathNode,originalJson.get(i),path,repalceNode,nextIndex+1);
			}
		} else {
			changeJsonValue(pathNode,originalJson,path,repalceNode,nextIndex+1);
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
