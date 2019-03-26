package org.unicorn.framework.oauth.component;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.util.http.HttpClientUtils;
import org.unicorn.framework.util.json.JsonUtils;

import java.util.HashMap;
import java.util.Map;


public class UnicornAccessTokenService {

    public OAuth2AccessToken oauthToken(String accessTokenUrl, Map<String, String> paramters, Map<String, String> headmap) throws PendingException {
        try {
            String reStr = HttpClientUtils.getInstance().httpPost(accessTokenUrl, paramters, headmap);
            ResponseEntity<OAuth2AccessToken> dd = JsonUtils.fromJson(reStr, new TypeReference<ResponseEntity<OAuth2AccessToken>>() {
            });
            if (HttpStatus.OK.value() == dd.getStatusCodeValue()) {
               return dd.getBody();
            }
        } catch (Exception e) {
             throw new PendingException(SysCode.SYS_FAIL,"认证授权登录失败");
        }

        return null;
    }

    public static void main(String[] args) {

        String url = "http://localhost:18081/oauth/token";
        Map<String, String> paramters = new HashMap<>();
        paramters.put("grant_type", "password");
        paramters.put("username", "admin");
        paramters.put("password", "123456");

        Map<String, String> headmap = new HashMap<>();
        headmap.put("Authorization", "Basic YmFja3N0YWdlOmJhY2tzdGFnZQ==");


        String re = HttpClientUtils.getInstance().httpPost(url, paramters, headmap);
        System.out.println(JsonUtils.toJson(re));
        try {
            ResponseEntity<OAuth2AccessToken> dd = JsonUtils.fromJson(re, new TypeReference<ResponseEntity<OAuth2AccessToken>>() {
            });
            if (HttpStatus.OK.value() == dd.getStatusCodeValue()) {
                System.out.println(JsonUtils.toJson(dd));
            }

        } catch (Exception e) {

        }

    }
}
