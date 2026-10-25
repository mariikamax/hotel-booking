package ru.vsu.cs.maximova.util;

public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isNullOrBlank(String source) {
        return source == null || source.isBlank();
    }
}