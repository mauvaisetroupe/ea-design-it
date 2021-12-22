package com.mauvaisetroupe.eadesignit.domain.util;

public class EnumUtil {

    public static String clean(String value) {
        if (value == null) return null;
        return value
            .replace('/', '_')
            .replace(' ', '_')
            .replace('(', '_')
            .replace(')', '_')
            .replace("__", "_")
            .replace("__", "_")
            .replaceAll("(.*)_$", "$1")
            .toUpperCase();
    }
}
