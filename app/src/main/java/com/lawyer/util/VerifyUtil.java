package com.lawyer.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ink.itwo.common.util.IToast;

/**
 @author wang
 on 2019/2/21 */

public class VerifyUtil {

    public static boolean isMobile(String mobileNo) {
        if (TextUtils.isEmpty(mobileNo)) {
            return false;
        }
        if (!mobileNo.matches("^(1)\\d{10}$")) {
            return false;
        }
        return true;
    }

    public static boolean isCheckCode(String code) {
        if (TextUtils.isEmpty(code) || code.length() != 6) {
            return false;
        }
        return true;
    }

    public static boolean isPassword(String password) {
        if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 10) {
            IToast.show("请输入6-10位密码");
            return false;
        }
        if (hasEmoji(password)) {
            IToast.show("密码不支持表情符号");
            return false;
        }

        return !TextUtils.isEmpty(password);
    }

    public static boolean isNickName(String nickName) {
        if (TextUtils.isEmpty(nickName)) return false;
        if (hasEmoji(nickName)) { return false;
        }
        return true;
    }

    /** 真实姓名 */
    public static boolean isRealName(String realName) {
        if (TextUtils.isEmpty(realName)) return false;
        if (hasEmoji(realName)) return false;
        return true;
    }

    /** 身份证号 */
    public static boolean isIdNumber(String idNumber) {
        if (TextUtils.isEmpty(idNumber)) return false;
        Pattern pattern = Pattern.compile(regex_id_number);
        Matcher matcher = pattern.matcher(idNumber);
        return matcher.matches();
    }

    /** true: 含有表情 */
    public static boolean hasEmoji(String content) {
        for (int i = 0; i < content.length(); i++) {
            if (!isNotEmojiCharacter(content.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /** 身份证正则 */
    public static final String regex_id_number = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

}
