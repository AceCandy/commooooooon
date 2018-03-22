package com.br.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;

public class MD5Utils {

    /**
     *
     * @param plainText 明文
     * @return 32位密文
     */
    public static String cell32(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    public static String cell8(String plainText) {
        return cell32(plainText).substring(8, 16);
    }

    public static String cell16(String plainText) {
        return cell32(plainText).substring(16);
    }

    /**
     * 取32的中间16位
     * @param plainText
     * @return
     */
    public static String md5_16(String plainText){
    	return StringUtils.substring(cell32(plainText), 8, 24);
    }
    public static byte[] md5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }

    public static String md5(byte[] data1, byte[] data2) throws Exception {
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        md5Digest.update(data1);
        md5Digest.update(data2);
        return bytesToString(md5Digest.digest());
    }

    public static String md5(byte[] data1, byte[] data2, byte[] data3) throws Exception {
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        md5Digest.update(data1);
        md5Digest.update(data2);
        md5Digest.update(data3);
        return bytesToString(md5Digest.digest());
    }

    public static String bytesToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toHexString(0x100 + (b & 0xff)).substring(1));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(cell32("abasfdgfasdfsadf"));
    }

}
