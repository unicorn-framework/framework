package org.unicorn.framework.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.unicorn.framework.oauth.dto.UnicornUser;
import org.unicorn.framework.oauth.properties.OAuth2Properties;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author  xiebin
 */
@Configuration
@EnableConfigurationProperties(OAuth2Properties.class)
public class UnicornTokenEnhancer implements TokenEnhancer {
    @Autowired
    private OAuth2Properties oAuth2Properties;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                                     OAuth2Authentication authentication) {
        if (accessToken instanceof DefaultOAuth2AccessToken) {
            DefaultOAuth2AccessToken token = ((DefaultOAuth2AccessToken) accessToken);


            UnicornUser user = (UnicornUser) authentication.getPrincipal();
            Map<String, Object> additionalInformation = new HashMap<String, Object>();
            additionalInformation.put("clientId", authentication.getOAuth2Request().getClientId());
            additionalInformation.put("userInfo", user);
            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if (refreshToken instanceof DefaultOAuth2RefreshToken) {
                token.setRefreshToken(new DefaultOAuth2RefreshToken(getNewToken(authentication.getOAuth2Request().getClientId(), user.getId())));
            }
            token.setValue(getNewToken(authentication.getOAuth2Request().getClientId(), user.getId()));
            token.setAdditionalInformation(additionalInformation);
            return token;
        }
        return accessToken;
    }

    /**
     * 生成token的规则
     * @param clientId
     * @param userId
     * @return
     */
    private String getNewToken(String clientId, Long userId) {
        return oAuth2Properties.getTokenPrefix() + ":" + UUID.randomUUID().toString().replace("-", "") + ":" + clientId + ":" + userId;
    }
}
