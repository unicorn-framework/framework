package org.unicorn.framework.oauth.handler;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author  xiebin
 */
@Order(11)
public class UnicornErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
                                                  boolean includeStackTrace) {
        Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();
        errorAttributes.put("resCode","9000");
        return errorAttributes;
    }


}
