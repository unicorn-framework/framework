package org.unicorn.framework.util.cron;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 根据时间生成cron表达式
 * @author  xiebin
 *
 */
public class CronUtil {

    private static final String CRON_DATE_FORMAT = "ss mm HH dd MM ? yyyy";

    /***
     * @param date 时间
     * @return cron类型的日期
     */
    public static String getCron(final Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
        String formatTimeStr = "";
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }
    public static void main(String [] args){
        System.out.println(getCron(new Date()));
    }
}
