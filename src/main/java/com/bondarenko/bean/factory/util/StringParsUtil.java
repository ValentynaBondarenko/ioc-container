package com.bondarenko.bean.factory.util;

public final class StringParsUtil {
    private StringParsUtil() {
    }

    public static String getClassNameFromPath(String pathToClass) {
        return pathToClass.substring(pathToClass.lastIndexOf(".") + 1);
    }
}
