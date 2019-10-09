package org.unicorn.framework.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.gateway.dto.BaseSecurityDto;
import org.unicorn.framework.util.hash.RSA;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 签名安全处理
 *
 * @author xiebin
 */
@Component
@Slf4j
public class SignSecurityHanlder extends AbstractSecurityHanlder {
    @Qualifier("redisTemplate")
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public void vaildation(BaseSecurityDto baseSecurityDto) throws PendingException {
        try {
            //检查sign是否存在缓存中
            if (redisTemplate.hasKey(genKey(baseSecurityDto))) {
                log.warn("未通过安全检查");
                //存在拒绝请求
                throw new PendingException(SysCode.API_SECURITY_ERROR);
            }
            cacheSign(redisTemplate, baseSecurityDto);
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
        return true;
    }

    @Override
    public Integer order() {
        return 1;
    }

    private void cacheSign(RedisTemplate<String, Object> redisTemplate, BaseSecurityDto baseSecurityDto) {
        //将sign存入缓存中
        redisTemplate.opsForValue().set(genKey(baseSecurityDto), baseSecurityDto.getSign());
        redisTemplate.expire(genKey(baseSecurityDto), getGatewaySecurityProperties().getDiffMinutes() * 60 + 10, TimeUnit.SECONDS);

    }

    /**
     * 生成key
     *
     * @param baseSecurityDto
     * @return
     */
    private String genKey(BaseSecurityDto baseSecurityDto) {
        String key = baseSecurityDto.getSign() + "_securtiy";
        String namespace = "gateway_security";
        return namespace + ":" + key;
    }
}
