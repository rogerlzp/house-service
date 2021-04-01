
package com.house.backend.houseservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Helen.Chen
 * @Title:StringUtils
 * @Description:String工具类
 * @date 2020/2/13
 */
public class StringUtils {
	private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

	private StringUtils() {
		throw new IllegalAccessError("Utility class");
	}

	public static boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	/**
	 * @param pStr
	 *            需要判断的值
	 * @Title:isEmpty
	 * @Description:是否为空
	 * @author Sherry.Wang
	 * @date 2017/1/12
	 */
	public static boolean isEmpty(String pStr) {
		return pStr == null || pStr.trim().length() == 0 || "null".equals(pStr);
	}

	public static boolean isNotEmpty(String pStr) {
		return !isEmpty(pStr);
	}

	public static String defaultIfBlank(String str, String defaultStr) {
		return isEmpty(str) ? defaultStr : str;
	}

	public static String defaultIfBlank(Object str, String defaultStr) {
		return null == str ? defaultStr : str.toString();
	}

	public static String substrIndex(String str, int index) {
		return str.length() > index ? str.substring(0, index) : str;
	}

	public static String notBlankStr(String str, String notEmptyStr) {
		return isEmpty(str) ? "" : notEmptyStr;
	}

	/**
	 * @param ch
	 *            需要转换的char
	 * @Title:charToByteAscii
	 * @Description:char转换成byte
	 * @author Sherry.Wang
	 * @date 2017/1/12
	 */
	public static byte charToByteAscii(char ch) {
		byte byteAscii = (byte) ch;
		return byteAscii;
	}

	/**
	 * @Title:copyModel
	 * @Description:拷贝属性
	 * @author Sherry.Wang
	 * @date 2017/1/12
	 */
	public static Object copyModel(Object classType, String[] parameters, Object o) {
		Object invoker = classType;
		Map<String, String> map = new HashMap<String, String>();
		try {
			Method method;
			Class<?> clazz = classType.getClass();
			// 根据Class对象获得属性 私有的也可以获得
			Field[] fields = clazz.getDeclaredFields();
			// 根据Class对象获得属性 私有的也可以获得
			Field[] ofields = o.getClass().getDeclaredFields();
			for (Field of : ofields) {
				map.put(of.getName(), of.getName());
			}
			for (String param : parameters) {
				for (Field f : fields) {
					if (f.getName().equals(param) && map.containsKey(param)) {
						String strFieldName = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
						if ((f.getType()).isAssignableFrom(String.class)) {
							method = classType.getClass().getMethod("set" + strFieldName, String.class);
							Method m = o.getClass().getMethod("get" + strFieldName);
							method.invoke(invoker, m.invoke(o));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("copyModel is error:::", e);
		}
		return invoker;
	}

	/**
	 * @Title:defIfEmpty
	 * @Description:判断为空的默认值
	 * @author Sherry.Wang
	 * @date 2017/1/12
	 */
	public static String defIfEmpty(String pStr, String pDefStr) {
		return isEmpty(pStr) ? pDefStr : pStr;
	}

	public static String format2RatioString(String value) {
		DecimalFormat formatter = new DecimalFormat("###############0.00");
		return (formatter.format(parseDouble(value)));
	}

	public static String format4RatioString(String value) {
		DecimalFormat formatter = new DecimalFormat("###############0.0000");
		return (formatter.format(parseDouble(value)));
	}
	public static String format5RatioString(String value) {
		DecimalFormat formatter = new DecimalFormat("###############0.00000");
		return (formatter.format(parseDouble(value)));
	}

	private static double parseDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			logger.error("parseDouble 错误", e);
			return 0;
		}
	}

	/**
	 * 分隔符转换
	 * @param pStr
	 * @return
	 */
	public static String transformSplit(String pStr){
		if (pStr.contains("|")) {
			pStr = pStr.replaceAll("\\|", "','");
		} else if (pStr.contains(";")) {
			pStr = pStr.replaceAll(";", "','");
		} else if (pStr.contains(",")) {
			pStr = pStr.replaceAll(",", "','");
		}
		if (StringUtils.isNotEmpty(pStr) && !pStr.startsWith("'")) {
            pStr = "'" + pStr;
        }
		if (StringUtils.isNotEmpty(pStr) && !pStr.endsWith("'")) {
			pStr = pStr + "'";
		}
		return pStr;
	}


	/**
	 * 正则表达式过滤字符串
	 * @param src
	 * @param regex
	 * @return
	 */
	public static String regexQuery(String src,String regex){
		Matcher matcher = Pattern.compile(regex).matcher(src);
		if(matcher.find()){
			return matcher.group(0).trim();
		}
		return null;
	}
	public static List<String> regexQueryList(String src, String regex){
		Matcher matcher = Pattern.compile(regex).matcher(src);
		List<String> result = new LinkedList<>();
		while(matcher.find()){
			result.add(matcher.group(0).trim());
		}
		return result;
	}

	/**
	 *
	 * @param oriString  原始传入的字符串
	 * @param match      匹配的字符串
	 * @param replace    要替换成的字符串
	 * @return
	 */
	public static String replaceLast(String oriString, String match, String replace) {
		if (StringUtils.isEmpty(oriString)) {
			// 非法字符串，直接返回
			return oriString;
		}

		StringBuilder stringBuilder = new StringBuilder(oriString);
		int lastIndexOf = stringBuilder.lastIndexOf(match);

		if (-1 == lastIndexOf) {
			// 不匹配，返回原始字符串
			return oriString;
		}

		return stringBuilder.replace(lastIndexOf, lastIndexOf+match.length(), replace).toString();
	}

	/**
	 * 判断字符串中是否包含中文
	 *
	 * @param str 待校验字符串
	 * @return 是否为中文
	 * @warn 不能校验是否为中文标点符号
	 */
	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		return m.find();
	}


	/**
	 * 正则表达式匹配两个指定字符串中间的内容
	 * @param soap
	 * @return
	 */
	public static List<String> getSubUtil(String soap,String rgex){
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(rgex);// 匹配的模式
		Matcher m = pattern.matcher(soap);
		while (m.find()) {
			int i = 1;
			list.add(m.group(i));
			i++;
		}
		return list;
	}

	/**
	 * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样
	 * @param soap
	 * @param rgex
	 * @return
	 */
	public static String getSubUtilSimple(String soap,String rgex){
		Pattern pattern = Pattern.compile(rgex);// 匹配的模式
		Matcher m = pattern.matcher(soap);
		if(m.find()){
			return m.group(1);
		}
		return "";
	}

	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "with  tmp_datetmp_date  as  \n" +
				" (select  max(data_date)\n" +
				"    from da.oc_area_market_kpi_day_all\n" +
				"   where hold_amt_with_income > 0\n" +
				"     ) \n" +
				"select";
		String rgex = "with([\\S\\s]*)as([\\S\\s]*)select";
		str=str.replaceAll(rgex,"with tmpa as "+getSubUtilSimple(str, rgex)+"select");
		//System.out.println(str);


		 rgex = "with([\\S\\s]*)as([\\S\\s]*)select";
		Pattern p = Pattern.compile(rgex);
		Matcher m = p.matcher(str);
		if (m.find()) {
			System.out.println("111");
		}

		//System.out.println(getSubUtil(str,rgex));
		//System.out.println(getSubUtilSimple(str, rgex));
	}



}
