package com.br.common.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

class BrCipherMakerOld {
	
	private BrCipherMakerOld(){}
	public static class BrCipherMakerHolder{
		public static BrCipherMakerOld instance = new BrCipherMakerOld();
	}
	
	public static BrCipherMakerOld getInstance(){
		return BrCipherMakerHolder.instance;
	}
	
//	static final char sp = 31;  因放入json，会默认编码，\u001F 所以换成特殊 Α
	static final String sp = "Α";
	
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
		if(StringUtils.isBlank(text) || !StringUtils.contains(text, sp))return text;
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
			off[i] = keyChar[i] - 'a';
		}
		int len = orginal.length();
		char[] orgChar = orginal.toCharArray();

		for (int i = 0, j = 0; i < len; i++) {
			if (orgChar[i] == ' ') {
				continue;
			}
			orgChar[i] += off[j % cyc];
			j++;
		}
		try {
			return Base64.encodeBase64URLSafeString(new String(orgChar).getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	static final byte[] CHUNK_SEPARATOR = { '\r', '\n' };
	
	private String decode(String text, String key) {
		try {
			text = new String(new Base64(0, CHUNK_SEPARATOR, true).decode(text), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		int cyc = key.length();
		char[] keyChar = key.toLowerCase().toCharArray();
		int[] off = new int[cyc];
		for (int i = 0; i < cyc; i++) {
			off[i] = keyChar[i] - 'a';
		}
		int len = text.length();
		char[] orgChar = text.toCharArray();

		for (int i = 0, j = 0; i < len; i++) {
			if (orgChar[i] == ' ') {
				continue;
			}
			orgChar[i] -= off[j % cyc];
			j++;
		}
		return new String(orgChar);

	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		
		
		BrCipherMakerOld pm = new BrCipherMakerOld();
		String pass = pm.encode("18656992610");
		
		String text = pm.decode(pass);
		System.out.println(pass);
		System.out.println(text);
		
	}
}
