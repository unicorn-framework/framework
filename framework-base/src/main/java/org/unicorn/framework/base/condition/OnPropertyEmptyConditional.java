package org.unicorn.framework.base.condition;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * property空属性Condition 处理
 *
 * @author zhanghaibo
 * @since 2020/5/13
 * @see ConditionalOnPropertyEmpty
 */
public class OnPropertyEmptyConditional extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {

        Map<String, Object> mapValue = metadata.getAnnotationAttributes(ConditionalOnPropertyEmpty.class.getName());
        AnnotationAttributes annotationAttributes = new AnnotationAttributes(mapValue);

        String[] values = annotationAttributes.getStringArray("value");
        boolean attach = annotationAttributes.getBoolean("attach");
        PropertyResolver propertyResolver = context.getEnvironment();

        //
        Set<String> valuesSet = Arrays.stream(values)
                // 过滤空值
                .filter(v -> !StringUtils.isEmpty(v))
                // containsProperty
                .filter(v -> propertyResolver.containsProperty(v) && attach)
                // 实际值判断
                .filter(v ->!StringUtils.isEmpty(propertyResolver.getProperty(v)))
                .collect(Collectors.toSet());

        // 如果为false，则属性名称对应的值不存在就匹配
        if (CollectionUtils.isEmpty(valuesSet) && !attach) {
            return ConditionOutcome.match(ConditionalOnPropertyEmpty.class.getName() + "->matched");
        }else if (CollectionUtils.isEmpty(valuesSet) && attach) {
            // 如果值为空就不匹配
           return ConditionOutcome.noMatch(ConditionMessage.of("no matched", valuesSet.size(), attach));
        }else if (valuesSet.size() == values.length) {
            // 如果都有值就匹配
            return ConditionOutcome.match(ConditionalOnPropertyEmpty.class.getName() + "->matched");
        }
        return ConditionOutcome.noMatch(ConditionMessage.of("no matched"));
    }
}
