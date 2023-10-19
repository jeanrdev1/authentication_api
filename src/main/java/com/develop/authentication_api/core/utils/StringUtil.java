package com.develop.authentication_api.core.utils;

public class StringUtil {

    public static Boolean isNull(String string1) {
        return string1 == null;
    }

    public static Boolean isNullOrEmpty(String string1) {
        return string1 == null || string1.equals("");
    }

    public static Boolean isNotNull(String string1) {
        return !isNull(string1);
    }
}
