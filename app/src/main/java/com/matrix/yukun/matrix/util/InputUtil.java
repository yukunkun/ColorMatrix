package com.matrix.yukun.matrix.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 输入框工具类
 *
 * @author LiuFeng
 * @date 2017-11-01
 */
public class InputUtil {

    /**
     * 判断是否是合法的账号（手机号或邮箱）
     *
     * @param account
     *
     * @return
     */
    public static boolean isAccountLegal(String account) {
        return !TextUtils.isEmpty(account) && account.matches("^(\\d{11})|([\\w\\.\\-_]+@[\\w-]+[\\.][\\w-]+[\\w-_.]*)$");
    }

    /**
     * 判断是否是合法的邮箱
     *
     * @param email
     *
     * @return
     */
    public static boolean isEmailLegal(String email) {
        return !TextUtils.isEmpty(email) && email.matches("[\\w\\.\\-_]+@[\\w-]+[\\.][\\w-]+[\\w-_.]*$");
    }

    /**
     * 判断是否是合法的手机号
     *
     * @param mobile
     *
     * @return
     */
    public static boolean isMobileLegal(String mobile) {
        return !TextUtils.isEmpty(mobile) && mobile.matches("^[1][345678][0-9]{9}$");
    }

    /**
     * 判断是否是合法的昵称
     *
     * @param nickName
     *
     * @return
     */
    public static boolean isNickNameLegal(String nickName) {
        return !TextUtils.isEmpty(nickName) && nickName.matches("^([\\u4e00-\\u9fa5\\w_-]*)$");
    }

    /**
     * 判断是否是合法的汉字、字母、数字
     *
     * @param CharNumber
     *
     * @return
     */
    public static boolean isCharNumberLegal(String CharNumber) {
        return !TextUtils.isEmpty(CharNumber) && CharNumber.matches("^([\\u4e00-\\u9fa5\\w]*)$");
    }

    /**
     * 判断是否是合法的校验码（规则：4位数字）
     *
     * @param checkCode
     *
     * @return
     */
    public static boolean isCheckCodeLegal(String checkCode) {
        return !TextUtils.isEmpty(checkCode) && checkCode.matches("\\d{4}");
    }

    /**
     * 判断是否是合法的密码：规则6-12数字和字母组成
     *
     * @param password
     *
     * @return
     */
    public static boolean isPasswordLegal(String password) {
        return !TextUtils.isEmpty(password) && password.matches("^[a-z0-9A-Z]{6,12}$");
    }

    /**
     * 隐藏邮箱@前面的4位字符
     *
     * @param email
     *
     * @return
     */
    public static String hideEmailText(String email) {
        return email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
    }

    /**
     * 隐藏手机号中间的4位数字
     *
     * @param mobile
     *
     * @return
     */
    public static String hideMobileText(String mobile) {
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 判断html是否包含转义字符 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *
     * @param htmlContent
     *
     * @return
     */
    public static boolean isHtmlTransformLegal(String htmlContent) {
        return !TextUtils.isEmpty(htmlContent) && htmlContent.matches("\\&[a-zA-Z]{1,10};");
    }

    /**
     * 判断包含中文
     *
     * @param str
     *
     * @return
     */
    public static boolean isChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }

    /**
     * 是否包含字母
     *
     * @param str
     *
     * @return
     */
    public static boolean isLetter(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }
}
