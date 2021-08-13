package com.orilx.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private TimeUtil(){};

    /**
     * 返回自1970-01-01T00：00：00Z的纪元以来的毫秒数
     * (Instant.now().toEpochMilli())
     */
    public static Long getTimeMilli(){
        return Instant.now().toEpochMilli();
    }

    /**
     * 获取当前时间(格式:yyyy-MM-dd HH:mm:ss)
     * @return 当前时间
     */
    public static String getTime(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 获取当前日期(格式:MMdd)
     * @return 当前日期
     */
    public static String getDate(){
        return LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("MMdd"));
    }

    /**
     * 前后两时间之差
     * @return 两时间之差（单位：秒）
     */
    public static int diffTimeMilli(Long time1, Long time2){
        return (int)(time1 - time2)/1000;
    }

    /**
     * 返回当前时间和传入时间之差
     * @param time 要比较的时间
     * @return 时间之差（单位：秒）
     */
    public static int diffTimeMilli(Long time){
        return (int)(getTimeMilli() - time)/1000;
    }
}
