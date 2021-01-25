package com.chanlin.ad.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 通过工具类
 *
 * @author dustforest
 */
public class CommonUtils {
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate getLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String formatDate(Date date) {
        return dateFormatter.format(getLocalDate(date));
    }

    public static String formatDateTime(Date date) {
        return dateTimeFormatter.format(getLocalDate(date));
    }

    public static String getDurationString(Date startDate, Date endDate) {
        long duration = endDate.getTime() - startDate.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration) - 24 * days;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - 60 * TimeUnit.MILLISECONDS.toHours(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - 60 * TimeUnit.MILLISECONDS.toMinutes(duration);

        String durationString = "小于1秒";
        if (days > 0) {
            durationString = days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (hours > 0) {
            durationString = hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (minutes > 0) {
            durationString = minutes + "分" + seconds + "秒";
        } else if (seconds > 0) {
            durationString = seconds + "秒";
        }

        return durationString;
    }

    public static Date getDateWithDateString(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

    public static void copyContentToClipboard(String content, Context context) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }
}
