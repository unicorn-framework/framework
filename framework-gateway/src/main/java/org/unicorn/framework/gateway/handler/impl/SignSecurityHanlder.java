package org.unicorn.framework.gateway.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicorn.framework.cache.cache.CacheService;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.gateway.dto.BaseSecurityDto;
import org.unicorn.framework.gateway.handler.AbstractSecurityHanlder;
import org.unicorn.framework.util.hash.RSA;

import java.util.concurrent.TimeUnit;

/**
 * 签名安全处理
 *
 * @author xiebin
 */
@Component
@Slf4j
public class SignSecurityHanlder extends AbstractSecurityHanlder {

    @Autowired
    private CacheService cacheService;

    @Override
    public void vaildation(BaseSecurityDto baseSecurityDto) throws PendingException {
        try {
            //检查sign是否存在缓存中
            if (cacheService.containsKey(genKey(baseSecurityDto), SECURITY_NAME_SPACE)) {
                log.warn("该签名已使用过");
                //存在拒绝请求
                throw new PendingException(SysCode.API_SECURITY_ERROR);
            }
            cacheSign(baseSecurityDto);
            //公钥验签
            boolean signFlag = RSA.verify(baseSecurityDto.toString(), baseSecurityDto.getSign(), super.keyPairMap().get("public"), "utf-8");
            if (!signFlag) {
                log.warn("签名未通过");
                throw new PendingException(SysCode.API_SECURITY_ERROR);
            }
        } catch (PendingException pe) {
            throw pe;
        } catch (Exception e) {
            log.error("验签失败", e);
            throw new PendingException(SysCode.API_SECURITY_ERROR);
        }
    }

    @Override
    public boolean supports(BaseSecurityDto baseSecurityDto) throws PendingException {
        return super.getGatewaySecurityProperties().getSignCheckEnable();
    }

    @Override
    public Integer order() {
        return 1;
    }

    private void cacheSign(BaseSecurityDto baseSecurityDto) {
        //将sign存入缓存中
        cacheService.put(genKey(baseSecurityDto), baseSecurityDto.getSign(), getGatewaySecurityProperties().getDiffMinutes() * 60 + 10, TimeUnit.SECONDS, SECURITY_NAME_SPACE);
    }

    /**
     * 生成key
     *
     * @param baseSecurityDto
     * @return
     */
    private String genKey(BaseSecurityDto baseSecurityDto) {
        String key = baseSecurityDto.getSign();
        return key;
    }
}
