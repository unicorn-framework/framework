package org.unicorn.framework.cache.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-19 15:03
 */
@Slf4j
public class SpelUtil {


    static ExpressionParser parser = new SpelExpressionParser();

    static ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 计算spel
     *
     * @param spELString
     * @param joinPoint
     * @return
     */
    public static String generateKeyBySpEL(String spELString, JoinPoint joinPoint) {
        try {
            //如果表达式为空则直接返回
            if (StringUtils.isEmpty(spELString)) {
                return spELString;
            }
            //获取方法前面
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            //解析参数名列表
            String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
            //构建spel表达式
            Expression expression = parser.parseExpression(spELString);
            EvaluationContext context = new StandardEvaluationContext();
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            //计算表达式的值并返回
            return expression.getValue(context).toString();
        } catch (Exception e) {
            log.warn("计算spel失败：{}", spELString);
            return null;
        }
    }
}
