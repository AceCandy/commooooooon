package com.br.common.util;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;


/**
 * @Description: 设备反欺诈流水号生成器
 *
 * @Company: 百融（北京）金融信息服务股份有限公司
 * @Author: hongliang.zuo
 * @Date: 2016年7月5日 下午2:54:54 
 * @version: 1.0
 *
 */
public class SwiftNumberUtils {
	
	public static final String F_CHAR31 = "#";
	
	private Map<String,String> PLATCODEMAP = new HashMap<String,String>();
	private Map<String,String> PLATTYPEMAP = new HashMap<String,String>();//与PLATCODEMAP对应
	private Map<String,String> EVENTCODEMAP = new HashMap<String,String>();
	private Map<String,String> EVENTTYPEMAP = new HashMap<String,String>();//与EVENTCODEMAP对应
	
	private String s = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public char[] endFix = s.toCharArray();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private SwiftNumberUtils(){
		PLATCODEMAP.put("0", "android");
		PLATCODEMAP.put("1", "web");
		PLATCODEMAP.put("2", "ios");
		
		EVENTCODEMAP.put("00", "antifraud_register");
		EVENTCODEMAP.put("01", "antifraud_login");
		EVENTCODEMAP.put("02", "antifraud_lend");
		EVENTCODEMAP.put("03", "antifraud_cash");
		
		Iterator itr = PLATCODEMAP.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<String, String> cur = (Map.Entry<String, String>)itr.next();
			PLATTYPEMAP.put(cur.getValue(), cur.getKey());
		}
		itr = EVENTCODEMAP.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<String, String> cur = (Map.Entry<String, String>)itr.next();
			EVENTTYPEMAP.put(cur.getValue(), cur.getKey());
		}
	}
	private static class SwiftNumberUtilsHolder{
		public static SwiftNumberUtils instence = new SwiftNumberUtils();
	}
	public static SwiftNumberUtils getInstence(){
		return SwiftNumberUtilsHolder.instence;
	}
	/**
	 * 根据编号获取平台类型
	 * @param platCode
	 * @return
	 */
	public String getPlatTypeByCode(String platCode){
		return PLATCODEMAP.get(platCode);
	}
	/**
	 * 根据编号获取事件类型
	 * @param eventCode
	 * @return
	 */
	public String getEventTypeByCode(String eventCode){
		return EVENTCODEMAP.get(eventCode);
	}
	/**
	 * （apicode（预留9位）+ 标志位（共13位）+ 40位设备标示 + 16位md5后的userName）  之前整体aes加密后转base64 共108位， 然后 + （当前毫秒 + 纳秒的后五位） 的十六进制（15位）+ 5位随机数字或者字母
		//平台类型、事件类型 在标志位
		长度 9 + 13 + 40 + 16
		（标志位根据实际情况使用，可预估少于10个占一位即可）
		样例：加密后总长度 128位
	 * @param json 样例：{"apiCode":"1234560","platType":"android","eventType":"antifraud_login","did":"1d8d4584f600fa6a281409aa000001559f04b054","userName":"test"}
	 * @return 返回的数据为长度 128位
	 */
	public String createSwiftNumber(JSONObject json){
		if(null != json){
//			Random random = new Random();
//			String randomStr = endFix[random.nextInt(endFix.length)]+"";
			String apiCode = json.getString("apiCode");
			String platType = json.getString("platType");
			String eventType = json.getString("eventType");
			String did = json.getString("did"); //设备号，是 device_id、IDFA、gid
			String userName = json.getString("userName");
			StringBuffer swiftNumber = new StringBuffer(31);
			swiftNumber.append(fixApiCode(apiCode));
			
			swiftNumber.append(PLATTYPEMAP.get(platType)).append(EVENTTYPEMAP.get(eventType)).append("0000000000");
			
			swiftNumber.append(fixDid(did)).append(MD5Utils.md5_16(userName));
			
			long n = System.currentTimeMillis();
			long nano = System.nanoTime();
			String end = "00000";
			if(nano > 10000){
				String nanoStr = String.valueOf(nano);
				end = StringUtils.substring(nanoStr, nanoStr.length()-5);
			}
			String idx = String.valueOf(n) + end;
			if(idx.length() > 18)
				idx = StringUtils.substring(idx, 0, 18);
			String ss = Long.toHexString(Long.parseLong(idx));
			ss = fixNanoTime(ss);
			
			return StringUtils.substring(AESAlgorithmUtil.encrypt(swiftNumber.toString())+ F_CHAR31 + ss + endRandom(), 0, 128);
		}else{
			return null;
		}
	}
	public String endRandom(){
		StringBuffer str = new StringBuffer(5);
		int m = endFix.length;
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			str.append(endFix[random.nextInt(m)]);
		}
		return str.toString();
	}
	/**
	 * 不够15位，以随机数补充，否则截取后15位
	 * @param src
	 * @return
	 */
	private String fixNanoTime(String src){
		int len = src.length();
		if(len > 15){
			src = StringUtils.substring(src, len - 15);
		}else if(len < 15){
			StringBuffer str = new StringBuffer(15);
			str.append(src);
			Random random = new Random();
			for (int i = 0; i < 15 - len; i++) {
				str.append(endFix[random.nextInt(endFix.length)]);
			}
			src = str.toString();
		}
		return src;
	}
	/**
	 * 根据密文流水号解析为明文流水号
	 * @param cipher
	 * @return 解析失败或者不合法将返回null
	 */
	public String getClearSwiftNumber(String cipher){
		return AESAlgorithmUtil.decrypt(StringUtils.split(cipher, F_CHAR31)[0]);
//		return AESAlgorithmUtil.decrypt(StringUtils.substring(cipher, 0, 108));
	}
	/**
	 * 填充apiCode，不足9位未末填充 # 超过9位，取前9位
	 * @param apiCode
	 * @return
	 */
	private String fixApiCode(String apiCode){
		return fixContent(apiCode, "#", 9);
	}
	
	/**
	 * 填充did，不够四十位补充到四十
	 * @param did
	 * @return
	 */
	private String fixDid(String did){
		return fixContent(did, "#", 40);
	}
	private String fixContent(String src,String fixStr,int maxLen){
		int len = src.length();
		if(len > maxLen){
			src = StringUtils.substring(src, 0,maxLen);
		}else if(len < maxLen){
			StringBuffer str = new StringBuffer(maxLen);
			str.append(src);
			for (int i = 0; i < maxLen - len; i++) {
				str.append(fixStr);
			}
			src = str.toString();
		}
		return src;
	}
	/**
	 * 根据明文流水解析apiCode
	 * @param swiftNumber
	 * @return
	 */
	public String getApiCodeByClearSwiftNumber(String swiftNumber){
		return StringUtils.replace(StringUtils.substring(swiftNumber, 0,9), "#", "");
	}
	/**
	 * 根据明文流水解析 platType
	 * @param swiftNumber
	 * @return
	 */
	public String getPlatTypeByClearSwiftNumber(String swiftNumber){
		return PLATCODEMAP.get(StringUtils.substring(swiftNumber, 9,10));
	}
	
	/**
	 * 根据明文流水解析 eventType
	 * @param swiftNumber
	 * @return
	 */
	public String getEventTypeByClearSwiftNumber(String swiftNumber){
		return EVENTCODEMAP.get(StringUtils.substring(swiftNumber, 10,12));
	}
	/**
	 * 根据明文流水解析did
	 * @param swiftNumber
	 * @return
	 */
	public String getDidByClearSwiftNumber(String swiftNumber){
		return StringUtils.replace(StringUtils.substring(swiftNumber, 22,62), "#", "");
	}
	/**
	 * 根据明文流水解析userName ，md5后的
	 * @param swiftNumber
	 * @return
	 */
	public String getUserNameByClearSwiftNumber(String swiftNumber){
		return StringUtils.substring(swiftNumber, 62,78);
	}
	
	/**
	 * 根据密文流水号解析流水号当时生成的时间
	 * @param swiftNumber
	 * @return 异常时返回 0
	 */
	public long getCreateTimeBySwiftNumber(String swiftNumber){
		String [] ends = StringUtils.split(swiftNumber,F_CHAR31);
		if(null != ends && ends.length > 1){
			String times = ends[1];
			times = StringUtils.substring(times, 0, 15);
			BigInteger srch = new BigInteger(times, 16);
			times = srch.toString();
			String tm = StringUtils.substring(times, 0,times.length()-5);
			return Long.parseLong(tm);
		}
		return 0L;
	}
	public static void main(String[] args) {
		
		System.out.println(MD5Utils.cell32("XlxEvRedgGhVvHR_q8iB8mifuZ8hG9vA9VVXke2lbvSurgQ8JJAdPDbRAmJLUxhlhMrF29XBNuTak-s8pqSNUWgTzCxnN6Mwnw-jkFKk-Lo#2123e07f8ddbfb7MWYnR"));
		
		System.out.println(DateUtils.format(new Date(1491032393138L), "yyyy-MM-dd HH:mm:ss.sss"));
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				JSONObject json = new JSONObject();
				for (int i = 0; i < 1; i++) {
					System.out.println("行 "+ i);
					json = new JSONObject();
					json.put("apiCode", "123456"+i);
					json.put("platType", "android");
					json.put("eventType", "antifraud_login");
					json.put("did", "28888888888888888888888888888881");
					json.put("userName", "test");
					
					System.out.println(json.toString());
					//1d8d4584f600fa6a281409aa000001559f04b054 
					String swift = getInstence().createSwiftNumber(json);
					System.out.println("加密后：" +swift);
					System.out.println("解密时间：" +getInstence().getCreateTimeBySwiftNumber("eIzNVlZCH0SqeHUu9wx3hwTMmk0bxnfarOXb2_O_ZWU2Zy0iPHH3QIiW9uHe5liA7W2BAe30TL6eHJqRKwhbqmgTzCxnN6Mwnw-jkFKk-Lo#211d1973890ca66BGSFo"));
					System.out.println("长度：" + swift.length() + " ===" + StringUtils.contains(swift, SwiftNumberUtils.F_CHAR31));
					
					String clearText = getInstence().getClearSwiftNumber(swift);
					System.out.println("解密后：" + clearText);
					if(clearText==null){
						System.out.println("解析错误");
					}
					
					System.out.println("apiCode: " + getInstence().getApiCodeByClearSwiftNumber(clearText));
					System.out.println("platType: " + getInstence().getPlatTypeByClearSwiftNumber(clearText));
					System.out.println("eventType: " + getInstence().getEventTypeByClearSwiftNumber(clearText));
					System.out.println("did: " + getInstence().getDidByClearSwiftNumber(clearText));
					System.out.println("userName: " + getInstence().getUserNameByClearSwiftNumber(clearText));
				}
				
			}
		}).start();

	}
	
}
