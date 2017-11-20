package com.example.user.utils;

import java.text.DecimalFormat;

/**
 * Created by HEKANG on 2017/2/22.
 */

public class TextFormat {

    public static String formatByte(long data) {
        DecimalFormat format = new DecimalFormat("##.##");
        if (data < 1024) {
            return data + "B";
        } else if (data < 1024 * 1024) {
            return format.format(data / 1024f) + "KB";
        } else if (data < 1024 * 1024 * 1024) {
            return format.format(data / 1024f / 1024f) + "M";
        } else if (data < 1024 * 1024 * 1024 * 1024) {
            return format.format(data / 1024f / 1024f / 1024f) + "G";
        } else {
            return "超出统计范围";
        }
    }
}
