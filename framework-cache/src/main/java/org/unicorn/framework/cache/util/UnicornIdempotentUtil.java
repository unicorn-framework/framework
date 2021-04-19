package org.unicorn.framework.cache.util;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.unicorn.framework.util.hash.MD5;
import org.unicorn.framework.util.json.JsonUtils;
import org.unicorn.framework.web.utils.web.RequestUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-19 15:03
 */
public class UnicornIdempotentUtil {
    /**
     * 计算幂等key值
     *
     * @param spELString
     * @param joinPoint
     * @return
     */
    public static String parserIdempotentKey(String spELString, JoinPoint joinPoint) {
        //获取spel解析值
        String idempotentKey = SpelUtil.generateKeyBySpEL(spELString, joinPoint);
        if (StringUtils.isNotBlank(idempotentKey)) {
            return idempotentKey;
        }
        //为空则对请求URI和请求参数进行MD5加密作为幂等的唯一参照值
        HttpServletRequest request = RequestUtil.getRrequest();
        StringBuilder builder = new StringBuilder(request.getServletPath());
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            builder.append(JsonUtils.toJson(args[i]));
        }
        return MD5.Encode16(builder.toString());
    }
}
