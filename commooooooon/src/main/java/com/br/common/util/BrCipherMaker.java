package com.br.common.util;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 加密解密工具类-位移</p>
 * 
 * @Company: 百融（北京）金融信息服务股份有限公司
 * @Author: hongliang.zuo
 * @Date: 2017年6月13日
 * @version: 1.0
 */
public class BrCipherMaker {
	
	private static final Logger log = LoggerFactory.getLogger(BrCipherMaker.class);
	
	private BrCipherMaker(){}
	public static class BrCipherMakerHolder{
		public static BrCipherMaker instance = new BrCipherMaker();
	}
	
	public static BrCipherMaker getInstance(){
		return BrCipherMakerHolder.instance;
	}
//	static final char sp = 31;  因放入json，会默认编码，\u001F 所以换成特殊 Α 升級版本換成 B
	static final String sp = "Β";
	static final String sp2 = "Α";
	
	private static final String[] keys = new String[]{"c849a06defd23bac","c9e50bfe3ccbe05a","025371b9fef1098f","0b74d38da0a8789d","89b66e0f0917d044","afb283dbd5a1c950","b898901aded8d6a9","385baab99a0038c6","17360f5c5de62d1e","9f057500d30f94fa"};
	private static final int keysLen = keys.length;
	
	public int indexFor(String src){
		int idx = src.hashCode() % keysLen;
		return Math.abs(idx);
	}
	
	/**
	 * 加密
	 * @param orginal
	 * @return
	 */
	public String encode(String orginal){
		if(StringUtils.isBlank(orginal))return orginal;
		int idx = indexFor(orginal);
		String key = keys[idx];
		String mw = encode(orginal, key);
		//随机埋入索引
		if(StringUtils.isNotBlank(mw)){
			int lt = mw.length();
//			int s = lt - 1 - new Random().nextInt(lt);
			int s = Math.abs(mw.hashCode() % lt);
			if(s == 0){
				s = 2;
			}
			String idxs = idx+"";
			mw = StringUtils.substring(mw, 0,s) + sp + idxs + StringUtils.substring(mw, s);
		}
		return mw;
	}
	
	/**
	 * 解密
	 * @param text
	 * @return 异常返回 null ，如果不是此工具加密的，原样返回
	 */
	public String decode(String text){
		if(StringUtils.isBlank(text) || !(StringUtils.contains(text, sp) || StringUtils.contains(text, sp2))){
			return text;
		}else if(StringUtils.contains(text, sp2)){
			return BrCipherMakerOld.getInstance().decode(text);
		}
		String key = "";
		//先解析索引位置
		String[] txts = StringUtils.split(text,sp);
		if(txts.length > 1){
			String src = txts[0];
			String srcEnd = txts[1];
			int idx = Integer.parseInt(StringUtils.substring(srcEnd, 0, 1));
			src = src + StringUtils.substring(srcEnd, 1);
			key = keys[idx];
			return decode(src, key);
		}
		
		return null;
	}
	
	private String encode(String orginal, String key) {
		int cyc = key.length();
		char[] keyChar = key.toLowerCase().toCharArray();
		int[] off = new int[cyc];
		for (int i = 0; i < cyc; i++) {
			off[i] = keyChar[i];
		}
		int len = orginal.length();
		char[] orgChar = orginal.toCharArray();

		for (int i = 0, j = 0; i < len; i++) {
//			if (orgChar[i] == ' ') {
//				continue;
//			}
			orgChar[i] = (char) (orgChar[i] ^ off[j % cyc]);
			j++;
		}
		try {
			return Base64.encodeBase64URLSafeString(new String(orgChar).getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("加密异常"+orginal,e);
		}
		return null;
	}

	static final byte[] CHUNK_SEPARATOR = { '\r', '\n' };
	
	private String decode(String text, String key) {
		try {
			text = new String(new Base64(0, CHUNK_SEPARATOR, true).decode(text), "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("解密异常"+text,e);
		}
		int cyc = key.length();
		char[] keyChar = key.toLowerCase().toCharArray();
		int[] off = new int[cyc];
		for (int i = 0; i < cyc; i++) {
			off[i] = keyChar[i];
		}
		int len = text.length();
		char[] orgChar = text.toCharArray();

		for (int i = 0, j = 0; i < len; i++) {
//			if (orgChar[i] == ' ') {
//				continue;
//			}
			orgChar[i] = (char) (orgChar[i] ^ off[j % cyc]);
			j++;
		}
		return new String(orgChar);

	}

	public static void main(String[] args) throws UnsupportedEncodingException {
//		String b = "A";
//		String c = "Α";
//		String a = "•▪Α";
		
		BrCipherMaker pm = new BrCipherMaker();
//		String pass = pm.encode("OgsNOΑ6g8EBzY0NWQ5OAw5CTM8OQ8Q77-_CWFnNmUKOzc3Ow");
//		String pass = pm.encode("5767625255763235940abcxXAmb8#Q");
		String pass = pm.encode("18656992610");
//		
//		System.out.println("dddf=="+URLDecoder.decode(obj.get("afdddd").toString(), "utf-8"));
		String text = pm.decode(pass);
		System.out.println(pass);
		System.out.println(text);
		
	}
}
