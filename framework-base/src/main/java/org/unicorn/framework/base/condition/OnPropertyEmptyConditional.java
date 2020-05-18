package org.unicorn.framework.base.condition;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

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

        String key = annotationAttributes.getString("value");
        boolean attach = annotationAttributes.getBoolean("attach");
        PropertyResolver propertyResolver = context.getEnvironment();

        //
        if (propertyResolver.containsProperty(key)) {
            String property = propertyResolver.getProperty(key);
            // 存在值 并且 valueIfExists
            if (property != null && property.trim().length() > 0 && attach) {

                return ConditionOutcome.match(ConditionalOnPropertyEmpty.class.getName() + "->matched");
            }
        } else if (!attach) {
            return ConditionOutcome.match(ConditionalOnPropertyEmpty.class.getName() + "attach -> matched");
        }
        return ConditionOutcome.noMatch(ConditionMessage.of("no matched", key, attach, propertyResolver.getProperty(key)));
    }
}
