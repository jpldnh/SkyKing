package com.sking.lib.utils;

import com.sking.lib.base.SKObject;

import java.math.BigDecimal;

/**
 * Created by 谁说青春不能错 on 2017/1/9.
 * BigDecimal 加减乘除运算，每种提供三组方法,以及数字取余，取精度
 */

public class SKBigDecimalUtil extends SKObject {

    /*-------------------加法 start-----------------------*/

    /**
     * 加法运算。四舍五入
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }

    /**
     * 加法运算。四舍五入
     *
     * @param v1     被加数
     * @param v2     加数
     * @param format 保留几位小数
     * @return 两个参数的和
     */
    public static String addFormat(String v1, String v2, int format) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).setScale(format, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 加法运算,返回类型BigDecimal
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static BigDecimal addGetDecimal(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2);
    }

    /*-------------------加法 end-----------------------*/

    /*-------------------减法 start-----------------------*/

    /**
     * 减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static String sub(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).toString();
    }

    /**
     * 减法运算,double
     *
     * @param v1     被减数
     * @param v2     减数
     * @param format 保留几位小数
     * @return 两个参数的差
     */
    public static String subFormat(double v1, double v2, int format) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).setScale(format, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 减法运算。返回BigDecimal
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static BigDecimal subGetDecimal(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2);
    }

    /*-------------------减法 end-----------------------*/

    /*-------------------乘法 start-----------------------*/

    /**
     * 提供精确的乘法运算,返回Bigdecimal
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static String mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).toString();
    }

    /**
     * 乘法运算
     *
     * @param v1     被乘数
     * @param v2     乘数
     * @param format 保留位数
     * @return 两个参数的积
     */
    public static String mul(String v1, String v2, int format) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).setScale(format, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 提供精确的乘法运算,返回Bigdecimal
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mulGetDecimal(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2);
    }

    /*-------------------乘法 end-----------------------*/

    /*-------------------除法 start-----------------------*/

    /**
     * 精确的除法运算
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static String div(String v1, String v2) {
        if (isNull(v2) || "0".equals(v2))
            return "";
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2).toString();
    }

    /**
     * 除法运算
     *
     * @param v1     被除数
     * @param v2     除数
     * @param format 表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static String divFormat(String v1, String v2, int format) {
        if (isNull(v2) || "0".equals(v2))
            return "";
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, format, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 精确的除法运算。除不尽时，由scale参数指 定精度 四舍五入。string
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static BigDecimal bigDiv(String v1, String v2) {
        if (isNull(v2) || "0".equals(v2))
            return new BigDecimal("0");
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2);
    }
    /*-------------------除法 end-----------------------*/

    /*-------------------其它-----------------------*/
    /**
     * 对一个数字取精度
     *
     * @param v
     * @param scale
     * @return
     */
    public static BigDecimal getAccuracy(String v, int scale) {
        BigDecimal b = new BigDecimal(v);
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 取余数  string
     *
     * @param v1
     * @param v2
     * @return string
     */
    public static String strRemainder2Str(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.remainder(b2).toString();
    }

    /**
     * 比较大小 如果v1 大于v2 则 返回true 否则false
     *
     * @param v1
     * @param v2
     * @return
     */
    public static boolean compareTo(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.compareTo(b2) > 0 ? true : false;
    }
}
