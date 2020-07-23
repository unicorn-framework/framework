package org.unicorn.framework.gateway.handler;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.gateway.dto.BaseSecurityDto;
import org.unicorn.framework.gateway.properties.UnicornGatewaySecurityProperties;

import java.util.Map;

/**
 * 安全处理
 *
 * @author xiebin
 */
public abstract class AbstractSecurityHanlder implements IGatewayHandler<BaseSecurityDto> {
    public static final String SECURITY_NAME_SPACE = "security:name:space:";

    @Autowired
    private UnicornGatewaySecurityProperties gatewaySecurityProperties;

    @Override
    public ResponseDto<?> execute(BaseSecurityDto baseSecurityDto) throws PendingException {
        vaildation(baseSecurityDto);
        return new ResponseDto<>();
    }

    /**
     * 获取密钥对
     *
     * @return
     */
    public Map<String, String> keyPairMap() {
        Map<String, String> map = Maps.newHashMap();
        return gatewaySecurityProperties.getKeyPair();
    }

    public UnicornGatewaySecurityProperties getGatewaySecurityProperties() {
        return gatewaySecurityProperties;
    }
}
