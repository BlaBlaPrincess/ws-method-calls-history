package com.github.blablaprincess.methodcallshistory.common.utils;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {

    public String cropByMaxLength(String str, int maxLength) {
        int length = str.length();
        if (length > maxLength) {
            int half = maxLength / 2;
            return str.substring(0, half - (half + 1) % 2) + "…" + str.substring(length - half, length);
        }
        return str;
    }

}
