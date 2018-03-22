package com.br.common.util;

import java.text.DecimalFormat;

public class StringUtils extends org.apache.commons.lang.StringUtils{
	
	public static Double objectToDouble(Object o) {
		if (o == null || o.toString().equals("")) {
			return 0.0;
		}
		return Double.parseDouble(o.toString());
	}

	public static Integer objectToInteger(Object o) {
		if (o == null || o.toString().equals("")) {
			return 0;
		}
		return Integer.parseInt(formatNumber(o));
	}

	public static Long objectToLong(Object o) {
		if (o == null || o.toString().equals("")) {
			return 0l;
		}
		return Long.parseLong(formatNumber(o));
	}
	
	/**
	 * 格式化一个数字字符串为9999999的格式,如果字符串无法格式化返回0
	 * 
	 * @param money
	 *            数字字符串
	 * @return 格式化好的数字字符串
	 */
	public static String formatNumber(Object object) {
		if (object == null || object.toString().equals("")) {
			return "0";
		}
		DecimalFormat myformat3 = new DecimalFormat();
		myformat3.applyPattern("0");
		String result = myformat3.format(Double.parseDouble(object.toString()));
		return result;
	}
	
	/**
	 * 格式化一个数字字符串为999.99的格式,如果字符串无法格式化返回0.00
	 * 
	 * @param money
	 *            数字字符串
	 * @return 格式化好的数字字符串
	 */
	public static String formatDouble(String data, int num) {
		String formatdata = "0.00";
		try {
			DecimalFormat myformat3 = new DecimalFormat();
			if (num == 2) {
				myformat3.applyPattern("##0.00");
			}
			if (num == 4) {
				myformat3.applyPattern("##0.0000");
			}
			double n = Double.parseDouble(data);
			formatdata = myformat3.format(n);
		} catch (Exception exception) {
			//
		}
		return formatdata;
	}
	
	/*
	 * 格式化数据针对1200 显示的是1000 1500显示的是2000
	 */
	public static String formatDouble(String data) {
		if ("".equals(data) || data == null) {
			return "0";
		}
		if (data.equals("-1")) {
			return "-1";
		} else if (data.equals("-1.0")) {
			return "-1.0";
		}
		long result = Math.round(Double.valueOf(data));
		long jd = 0;
		int b1 = 0;
		String resulttmp = result + "";
		if (resulttmp.length() > 1) {
			if (Integer.parseInt(resulttmp.charAt(1) + "") > 4) {
				b1 = Integer.parseInt(resulttmp.charAt(0) + "") + 1;
			} else {
				b1 = Integer.parseInt(resulttmp.charAt(0) + "");
			}
			jd = (long) (b1 * Math.pow(10, resulttmp.length() - 1));
		} else {
			jd = result;
		}
		return jd + "";
	}
	
	/**
	 * 判断是否为空
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value){
		if(value==null||value.length()==0){
			return true;
		}
		return false;
	}
	
	public static boolean isNotEmpty(String value){
		return !isEmpty(value);
	}
	
	public static boolean isJson(String result) {
		if ((result.startsWith("{") && result.endsWith("}"))
				|| ((result.startsWith("[") && result.endsWith("]")))) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isNotJson(String result) {
		if (result.startsWith("{") && result.endsWith("}")) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isValue(String value) {
		if (isBlank(value) || "-1".equals(value)) {
			return false;
		}
		return true;
	}
	
	/*
	 * 格式化距离的方法，只输出小数点后一位并且，对其进行四舍五入
	 */
	public static String formatbdData(String data, int num) {// 四舍五入
		String formatdata = "0.0";
		try {
			DecimalFormat myformat3 = new DecimalFormat();
			if (num == 2) {
				myformat3.applyPattern("##0.0");
			}
			double n = Double.parseDouble(data);
			formatdata = myformat3.format(n);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("对百度数据进行精度的处理出现问题");
		}
		return formatdata;
	}
	/**
	 * 在原有判断空的逻辑上，去掉NULL字符串(忽略大小写)
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
        boolean blank = !StringUtils.isBlank(str);
        if(blank){
        	if(StringUtils.equalsIgnoreCase(str, "null")){
        		blank = false;
        	}
        }
        return blank;
    }
	public static void main(String[] args) {
		System.out.println(StringUtils.isNotEmpty(""));
	}
}
