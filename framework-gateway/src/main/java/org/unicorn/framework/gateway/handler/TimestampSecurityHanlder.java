//package org.unicorn.framework.gateway.handler;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.unicorn.framework.core.SysCode;
//import org.unicorn.framework.core.exception.PendingException;
//import org.unicorn.framework.gateway.dto.BaseSecurityDto;
//import org.unicorn.framework.gateway.properties.UnicornGatewaySecurityProperties;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//import java.util.TimeZone;
//
///**
// * 请求时间安全处理
// *
// * @author xiebin
// */
//@Component
//@Slf4j
//public class TimestampSecurityHanlder extends AbstractSecurityHanlder {
//
//    @Autowired
//    private UnicornGatewaySecurityProperties gatewaySecurityProperties;
//
//    @Override
//    public void vaildation(BaseSecurityDto baseSecurityDto) throws PendingException {
//
//        //当前的时间
//        LocalDateTime currentTime = LocalDateTime.now();
//        //请求毫秒数
//        Instant instant = new Date(new Long(baseSecurityDto.getTimestamp())).toInstant();
//        //接口请求时间
//        LocalDateTime requestTime = LocalDateTime.ofInstant(instant, TimeZone.getTimeZone("GMT+8").toZoneId());
//        //时间偏移安全秒数
//        requestTime = requestTime.plusSeconds(gatewaySecurityProperties.getDiffMinutes() * 60);
//        //如果时间差 diffMinutes分钟则抛出异常，拒绝访问
//        if (requestTime.isBefore(currentTime)) {
//            log.warn("时间检查未通过");
//            throw new PendingException(SysCode.API_SECURITY_ERROR);
//        }
//    }
//
//    @Override
//    public boolean supports(BaseSecurityDto baseSecurityDto) throws PendingException {
//        return super.getGatewaySecurityProperties().getTimeStampCheckEnable();
//    }
//
//    @Override
//    public Integer order() {
//        return 2;
//    }
//
//    public static void main(String[] args) {
//        Instant now = Instant.ofEpochMilli(1587350491567L);
//        LocalDateTime date = LocalDateTime.ofInstant(now, TimeZone.getTimeZone("GMT+8").toZoneId());
//        LocalDateTime dateTime = LocalDateTime.parse("2019-09-12 14:12:01", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        System.out.println(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        System.out.println(System.currentTimeMillis());
//    }
//}
