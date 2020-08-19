package com.zhaoyunqi.kit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 身份证工具类
 *
 * @create 2018-07-07 下午2:15
 **/
public class IdCardUtil {

    private static final int length = 18;

    /**
     * 省(直辖市)码表
     */
    private static String[] provinceCode = {"11", "12", "13", "14", "15", "21", "22",
            "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
            "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63",
            "64", "65", "71", "81", "82", "91"};

    /**
     * 身份证前17位每位加权因子
     */
    private static int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 身份证第18位校检码
     */
    private static String[] refNumber = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    /**
     * 二代身份证号码有效性校验
     *
     * @param idNo
     * @return
     */
    public static boolean check(String idNo) {
        return isIdNoPattern(idNo) && isValidProvinceId(idNo.substring(0, 2)) && isValidDate(idNo.substring(6, 14)) && checkIdNoLastNum(idNo);
    }

    /**
     * 二代身份证正则表达式
     *
     * @param idNo
     * @return
     */
    private static boolean isIdNoPattern(String idNo) {
        return Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$", idNo);
    }

    /**
     * 检查身份证的省份信息是否正确
     *
     * @param provinceId
     * @return
     */
    private static boolean isValidProvinceId(String provinceId) {
        for (String id : provinceCode) {
            if (id.equals(provinceId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断日期是否有效
     *
     * @param inDate
     * @return
     */
    private static boolean isValidDate(String inDate) {
        if (inDate == null) {
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        if (inDate.trim().length() != dateFormat.toPattern().length()) {
            return false;
        }
        //执行严格的日期匹配
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 计算身份证的第十八位校验码
     *
     * @param cardIdArray
     * @return
     */
    private static String sumPower(int[] cardIdArray) {
        int result = 0;
        for (int i = 0; i < power.length; i++) {
            result += power[i] * cardIdArray[i];
        }
        return refNumber[(result % 11)];
    }

    /**
     * 校验身份证第18位是否正确(只适合18位身份证)
     *
     * @param idNo
     * @return
     */
    private static boolean checkIdNoLastNum(String idNo) {
        if (idNo.length() != length) {
            return false;
        }
        char[] tmp = idNo.toCharArray();
        int[] cardIdArray = new int[tmp.length - 1];
        for (int i = 0; i < tmp.length - 1; i++) {
            cardIdArray[i] = Integer.parseInt(tmp[i] + "");
        }
        String checkCode = sumPower(cardIdArray);
        String lastNum = tmp[tmp.length - 1] + "";
        if (Objects.equals(lastNum, "x")) {
            lastNum = lastNum.toUpperCase();
        }
        return Objects.equals(checkCode, lastNum);
    }

    /**
     * 根据身份编号获取生日
     *
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirthByIdCard(String idCard) {
        return idCard.substring(6, 14);
    }

    /**
     * 根据身份编号获取性别
     *
     * @param idCard 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static int getGenderByIdCard(String idCard) {
        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            return 1;//男
        } else {
            return 2;//女
        }
    }

    /**
     * 根据身份编号获取生日年
     *
     * @param idCard 身份编号
     * @return 生日(yyyy)
     */
    public static Short getYearByIdCard(String idCard) {
        return Short.valueOf(idCard.substring(6, 10));
    }

    /**
     * 根据身份编号获取生日月
     *
     * @param idCard
     *            身份编号
     * @return 生日(MM)
     */
    public static Short getMonthByIdCard(String idCard) {
        return Short.valueOf(idCard.substring(10, 12));
    }

    /**
     * 根据身份编号获取生日天
     *
     * @param idCard
     *            身份编号
     * @return 生日(dd)
     */
    public static Short getDateByIdCard(String idCard) {
        return Short.valueOf(idCard.substring(12, 14));
    }
}
