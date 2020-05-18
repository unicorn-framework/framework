package org.unicorn.framework.util.common;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author xiebin
 */
public class LocalDateUtils {


    /**
     * 时间差
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static Duration timeDiff(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime);

    }

    /**
     * String 转换 LocalDateTime
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime stringConvertLocalDateTime(String dateTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, df);
    }

    /**
     * String 转换 LocalDateTime
     *
     * @param dateTime
     * @param pattern
     * @return
     */
    public static LocalDateTime stringConvertLocalDateTime(String dateTime, String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTime, df);
    }

    /**
     *  date 转LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime dateConvertLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime 转 date
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeConvertDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

    }

    public static void main(String args[]) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime.of(2019, 9, 23, 8, 30, 0);
        System.out.println(timeDiff(LocalDateTime.of(2019, 9, 23, 8, 30, 0), localDateTime).toMinutes());
    }
}
