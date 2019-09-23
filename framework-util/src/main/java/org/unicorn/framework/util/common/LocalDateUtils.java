package org.unicorn.framework.util.common;

import java.time.Duration;
import java.time.LocalDateTime;

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
    public  static Duration timeDiff(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime);

    }

    public static void main(String args[]) {
        LocalDateTime localDateTime=LocalDateTime.now();
        LocalDateTime.of(2019,9,23,8,30,0);
        System.out.println(timeDiff( LocalDateTime.of(2019,9,23,8,30,0),localDateTime).toMinutes());
    }
}
