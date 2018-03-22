import java.util.regex.Pattern;


public class TestJsonType {

	public static void main(String[] args) {
		String str="{\"cell\":[\"13840783486\"],\"id\":\"510102197203027028\",\"name\":\"薛颖\",\"ExtData\":{},\"meal\":\"Execution\"}";
		String str1="{\"api_code\":\"1256561\",\"request_json\":\"{\\\"name\\\":\\\"孙琦\\\",\\\"id\\\":\\\"522522198402280021\\\",\\\"cell\\\":[\\\"13806583424\\\"],\\\"meal\\\":\\\"AccountChange_c,Consumption,scorebank\\\",\\\"cell_md5\\\":[\\\"c796f9176a3b428cf533d2ada7ab42eb\\\"]}\",\"request_ip\":\"10.20.2.138\",\"request_time\":1497577259679,\"msg_type\":\"request\",\"status\":1,\"swift_number\":\"1256561_20170616094059_7445\",\"response_time\":1497577262917,\"response_json\":\"{\\\"swift_number\\\":\\\"1256561_20170616094059_7445\\\",\\\"Flag\\\":{\\\"score\\\":\\\"99\\\",\\\"consumption\\\":\\\"1\\\",\\\"accountchange_c\\\":\\\"99\\\"},\\\"code\\\":\\\"00\\\",\\\"Score\\\":{}}\",\"t_cost\":3300,\"response_code\":\"00\"}";
		String str2="{\\\"name\\\":\\\"孙琦\\\",\\\"id\\\":\\\"522522198402280021\\\",\\\"cell\\\":[\\\"13806583424\\\"],\\\"meal\\\":\\\"AccountChange_c,Consumption,scorebank\\\",\\\"cell_md5\\\":[\\\"c796f9176a3b428cf533d2ada7ab42eb\\\"]}";
		System.out.println(isJsonObject(str2));
	}
	
	public static boolean isJsonObject(final String jsonStr) {
        if (jsonStr == null) return false;
        return Pattern.matches("^\\{.*\\}$", jsonStr.trim());
    }
	
	public static boolean isJsonArray(final String jsonStr) {
        if (jsonStr == null) return false;
        return Pattern.matches("^\\[.*\\]$", jsonStr.trim());
    }
	
}
