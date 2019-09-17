package org.unicorn.framework.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.gateway.dto.BaseSecurityDto;
import org.unicorn.framework.gateway.properties.UnicornGatewaySecurityProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 请求时间安全处理
 *
 * @author xiebin
 */
@Component
@Slf4j
public class TimestampSecurityHanlder extends AbstractSecurityHanlder {

    @Autowired
    private UnicornGatewaySecurityProperties gatewaySecurityProperties;

    @Override
    public void vaildation(BaseSecurityDto baseSecurityDto) throws PendingException {

        //当前的时间
        LocalDateTime currentTime = LocalDateTime.now();
        //接口请求时间
        LocalDateTime requestTime = LocalDateTime.parse(baseSecurityDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //时间偏移安全秒数
        requestTime = requestTime.plusSeconds(gatewaySecurityProperties.getDiffMinutes() * 60);
        //如果时间差 diffMinutes分钟则抛出异常，拒绝访问
        if (requestTime.isBefore(currentTime)) {
            log.warn("时间检查未通过");
            throw new PendingException(SysCode.API_SECURITY_ERROR);
        }
    }

    @Override
    public boolean supports(BaseSecurityDto baseSecurityDto) throws PendingException {
        return true;
    }

    @Override
    public Integer order() {
        return 2;
    }

    public static void main(String[] args) {

        LocalDateTime dateTime = LocalDateTime.parse("2019-09-12 14:12:01", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
