package org.unicorn.framework.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,AuthenticationException authException)  throws ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            ResponseDto resDto= new ResponseDto<>(SysCode.SESSION_ERROR);
            resDto.setUrl(request.getRequestURL().toString());
            if(authException.getCause() instanceof AccessDeniedException){
                resDto.setResCode(SysCode.UNAUTHOR__ERROR.getCode());
                resDto.setResInfo(SysCode.UNAUTHOR__ERROR.getInfo());
            }
//            if(authException instanceof InsufficientAuthenticationException){
//                resDto.setResCode(SysCode.UNAUTHOR__ERROR.getCode());
//                resDto.setResInfo(SysCode.UNAUTHOR__ERROR.getInfo());
//            }
            objectMapper.writeValue(response.getOutputStream(), resDto);
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}