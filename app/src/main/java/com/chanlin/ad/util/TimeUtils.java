package com.chanlin.ad.util;

import java.util.Date;

public class TimeUtils {
    public static String getAge(Date startDate, Date endDate) {
        String interval = "";

        // ms
        long time = endDate.getTime() - startDate.getTime();

        if (time/1000 < 10 && time/1000 >= 0) {
            interval = "刚刚";

        } else if (time/1000 < 60 && time/1000 > 0) {
            int se = (int) ((time%60000)/1000);
            interval = se + " 秒前";

        } else if (time/60000 < 60 && time/60000 > 0) {
            int m = (int) ((time%3600000)/60000);
            interval = m + " 分钟前";

        } else if (time/3600000 < 24 && time/3600000 > 0) {
            int h = (int) (time/3600000);
            interval = h + " 小时前";

        } else {
            int d = (int) (time/(3600000 * 24));
            if (d < 30) {
                interval = d + " 天前";
            } else {
                int month = d / 30;
                if (month < 12) {
                    interval = month + " 个月前";
                } else {
                    int year = month / 12;
                    interval = year + " 年前";
                }
            }
        }

        return interval;
    }
}