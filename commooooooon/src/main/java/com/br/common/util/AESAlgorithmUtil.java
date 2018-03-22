package com.br.common.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: aes加密解密算法
 *
 * @Company: 百融（北京）金融信息服务股份有限公司
 * @Author: hongliang.zuo
 * @Date: 2016年7月7日 下午1:43:00 
 * @version: 1.0
 *
 */
public abstract class AESAlgorithmUtil {
	private static final String PASSWORD_KEY = "435b0f792a94c043fb22314e60c2e8b1";

	private static final Logger log = LoggerFactory.getLogger(AESAlgorithmUtil.class);

	private static final String encode = System.getProperty("file.encoding");

	/**
	 * AES加密
	 * @param content
	 * @param pwdKey
	 * @return
	 *
	 * @author 
	 * @date May 9, 2012 9:26:14 AM 
	 */
	public static String encrypt(String content, String pwdKey) {
		if (StringUtils.isBlank(content) || StringUtils.isBlank(pwdKey)) {
			throw new IllegalArgumentException("参数不合法");
		}

		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// kgen.init(128, new SecureRandom(PASSWORD_KEY.getBytes()));
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(pwdKey.getBytes());
			kgen.init(128, secureRandom);

			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes(encode);
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			// System.out.println("======"+Base64.encodeBase64String(result));
			// System.out.println("======"+new String(Base64.encodeBase64(result, false, false, 300)));
			// System.out.println("======"+Base64.encodeBase64URLSafeString(result));
			// return Base64.encodeBase64String(result);
			return Base64.encodeBase64URLSafeString(result);
			// return parseByte2HexStr(result);
			// return Hex.encodeHexString(result);
		} catch (Exception e) {
			log.error("AES加密异常", e);
		}

		return null;
	}

	/**
	 * AES加密
	 * @param content
	 * @return
	 *
	 * @author 
	 * @date Sep 13, 2010 6:00:32 PM 
	 */
	public static String encrypt(String content) {
		return encrypt(content, PASSWORD_KEY);
	}
	/**
	 * AES解密
	 * @param content
	 * @return
	 *
	 * @author 
	 * @date Sep 13, 2010 6:03:31 PM 
	 */
	public static String decrypt(String content) {
		return decrypt(content, PASSWORD_KEY);
	}
	/**
	 * AES解密
	 * @param content
	 * @param pwdKey
	 * @return
	 *
	 * @author 
	 * @date Sep 13, 2010 6:03:31 PM 
	 */

	static final byte[] CHUNK_SEPARATOR = {'\r', '\n'};

	public static String decrypt(String content, String pwdKey) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// kgen.init(128, new SecureRandom(PASSWORD_KEY.getBytes()));

			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(pwdKey.getBytes());
			kgen.init(128, secureRandom);

			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化

			byte[] result = cipher.doFinal(new Base64(0, CHUNK_SEPARATOR, true).decode(content));
			// byte[] result = cipher.doFinal(Base64.decodeBase64(content));
			// byte[] result = cipher.doFinal(parseHexStr2Byte(content));
			// byte[] result = cipher.doFinal(Hex.decodeHex(content.toCharArray()));

			return new String(result, encode);
		} catch (Exception e) {
			log.error("AES解密异常");
		}

		return null;
	}

	/**
	 * 将二进制转换成16进制
	 * @param buf
	 * @return
	 *
	 * @author 
	 * @date Sep 13, 2010 6:01:27 PM 
	 */
	@SuppressWarnings("unused")
	private static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * @param hexStr
	 * @return
	 *
	 * @author 
	 * @date Sep 13, 2010 6:02:40 PM 
	 */
	@SuppressWarnings("unused")
	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		}

		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}

		return result;
	}

	public static void main(String[] args) {
		try {
			String content = "";

			for (int i = 0; i < 10000; i++) {
				content = "1234567##01010000006839119083" + i;
				String en = encrypt(content);
				System.out.println("加密后：" + en);
				String de = decrypt(en);
				System.out.println("解密后" + de);
				boolean eq = false;
				eq = StringUtils.equals(content, de);
				System.out.println("还原后是否相等：" + eq);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
