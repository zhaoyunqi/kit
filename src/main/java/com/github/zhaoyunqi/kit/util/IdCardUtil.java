package com.github.zhaoyunqi.kit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.regex.Pattern;

public class IdCardUtil {

    private static final int length = 18;

    private static String[] provinceCode = {"11", "12", "13", "14", "15", "21", "22",
            "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
            "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63",
            "64", "65", "71", "81", "82", "91"};

    private static int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    private static String[] refNumber = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    public static boolean check(String idNo) {
        return isIdNoPattern(idNo) && isValidProvinceId(idNo.substring(0, 2)) && isValidDate(idNo.substring(6, 14)) && checkIdNoLastNum(idNo);
    }

    private static boolean isIdNoPattern(String idNo) {
        return Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$", idNo);
    }

    private static boolean isValidProvinceId(String provinceId) {
        for (String id : provinceCode) {
            if (id.equals(provinceId)) {
                return true;
            }
        }
        return false;
    }

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

    private static String sumPower(int[] cardIdArray) {
        int result = 0;
        for (int i = 0; i < power.length; i++) {
            result += power[i] * cardIdArray[i];
        }
        return refNumber[(result % 11)];
    }

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

    public static String getBirthByIdCard(String idCard) {
        return idCard.substring(6, 14);
    }


    public static int getGenderByIdCard(String idCard) {
        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            return 1;//男
        } else {
            return 2;//女
        }
    }


    public static Short getYearByIdCard(String idCard) {
        return Short.valueOf(idCard.substring(6, 10));
    }

    public static Short getMonthByIdCard(String idCard) {
        return Short.valueOf(idCard.substring(10, 12));
    }

    public static Short getDateByIdCard(String idCard) {
        return Short.valueOf(idCard.substring(12, 14));
    }
}
