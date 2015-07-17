package com.teradata.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类
 *
 * @author liuyueteng
 * @data 2014/6/19
 */
public class CommonUtil {
    // 精度
    public final static class PRECISIONS {
        public final static String DOT_TOW = "#####.##";
    }

    /**
     * 获取指定位数的随机数
     *
     * @param digit
     * @return
     */
    public static String getRandom(int digit) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digit; i++) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }

    /**
     * 数据格式化
     *
     * @param value
     * @param divisor
     * @param formatStr
     * @return
     */
    public static String numberFormat(String value, String divisor, String formatStr) {
        if (value == null || value.length() == 0 || divisor == null || divisor.length() == 0 || formatStr == null
                || formatStr.length() == 0) {
            return value;
        }
        BigDecimal result = null;
        try {
            result = new BigDecimal(value).divide(new BigDecimal(divisor));
        } catch (ArithmeticException ae) {
            result = new BigDecimal(value).divide(new BigDecimal(divisor), BigDecimal.ROUND_UP);
        }
        DecimalFormat format = new DecimalFormat(formatStr);
        return format.format(result);
    }

    /**
     * 返回字符串或者为空时的默认值
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static String getValue(String value, String defaultValue) {
        if (value == null || value.length() == 0)
            return defaultValue;
        else
            return value;
    }

    /**
     * 返回Map中对应的key的value
     *
     * @param map
     * @param key
     * @return
     */
    public static String getString(Map map, String key) {
        if (map == null || map.size() == 0 || !map.containsKey(key))
            return null;
        return map.get(key).toString();
    }

    /**
     * 返回Map中对应的key的value，若Map为空或者没有该值，返回默认值
     *
     * @param map
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Map map, String key, String defaultValue) {
        if (map == null || map.size() == 0 || !map.containsKey(key))
            return defaultValue;
        return map.get(key).toString();
    }

    /**
     * 返回source字符串中匹配regex的所有子串
     *
     * @param source
     * @param regex
     * @return
     */
    public static String[] stringMatch(String source, String regex) {
        if (source == null || source.length() == 0)
            return null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        List<String> temp = new ArrayList<String>();
        while (matcher.find())
            temp.add(matcher.group());
        return temp.toArray(new String[0]);
    }

    /**
     * 返回source字符串中匹配regex的所有子串
     *
     * @param source
     * @param regex
     * @return
     */
    public static boolean stringMatches(String source, String regex) {
        if (source == null || source.length() == 0)
            return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        if (matcher.find())
            return true;
        return false;
    }

    public static class Date {
        public static String getStandardDate() {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());
        }

        public static String getStandardTimestamp() {
            return new SimpleDateFormat("yyyyMMddhhmmss").format(new java.util.Date());
        }

        public static String getStandardTimestamp0() {
            return new SimpleDateFormat("MMddhhmmss").format(new java.util.Date());
        }

        public static String getStandardTimestamp(int size) {
            String str = new SimpleDateFormat("yyyyMMddhhmmss").format(new java.util.Date());
            int startIndex = str.length() - size;
            return startIndex < 0 ? str : str.substring(startIndex);
        }
    }
}
