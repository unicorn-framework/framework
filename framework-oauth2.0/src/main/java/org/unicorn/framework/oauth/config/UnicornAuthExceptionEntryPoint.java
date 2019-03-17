package org.unicorn.framework.oauth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiebin
 */
public class UnicornAuthExceptionEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,AuthenticationException authException)  throws ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), new ResponseDto<>(SysCode.SESSION_ERROR));
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}