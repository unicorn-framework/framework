package org.unicorn.framework.base.config.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author xiebin
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        return JsoupUtil.clean(super.getHeader(name));
    }

    @Override
    public String getQueryString() {
        return JsoupUtil.clean(super.getQueryString());
    }

    @Override
    public String getParameter(String name) {
        return JsoupUtil.clean(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapseValues[i] = JsoupUtil.clean(values[i]);
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }
}
