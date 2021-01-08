package org.unicorn.framework.oauth.config;

import org.unicorn.framework.oauth.constants.OauthConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-01-08 10:08
 */
public class DeleteAuthorizationHttpServiceRequestWrapper extends HttpServletRequestWrapper {

    public DeleteAuthorizationHttpServiceRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> headerNameSet = new HashSet<>();
        Enumeration<String> wrappedHeaderNames = super.getHeaderNames();
        while (wrappedHeaderNames.hasMoreElements()) {
            String headerName = wrappedHeaderNames.nextElement();
            if (!OauthConstants.AUTHORIZATION.equalsIgnoreCase(headerName)) {
                headerNameSet.add(headerName);
            }
        }
        return Collections.enumeration(headerNameSet);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (OauthConstants.AUTHORIZATION.equalsIgnoreCase(name)) {
            return Collections.emptyEnumeration();
        }
        return super.getHeaders(name);
    }

    @Override
    public String getHeader(String name) {
        if (OauthConstants.AUTHORIZATION.equalsIgnoreCase(name)) {
            return null;
        }
        return super.getHeader(name);
    }


}
