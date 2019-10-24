
package org.unicorn.framework.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.unicorn.framework.base.base.AbstractService;
import org.unicorn.framework.base.base.UnicornContext;
import org.unicorn.framework.base.constants.UnicornConstants;
import org.unicorn.framework.core.dto.RequestInfoDto;
import org.unicorn.framework.core.dto.ResponseInfoDto;
import org.unicorn.framework.util.http.RequestUtils;
import org.unicorn.framework.util.json.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author xiebin
 */
@Aspect
@Component
@ConditionalOnProperty(prefix = "unicorn.api.log", name = "enable", havingValue = "true")
public class LogAspect extends AbstractService {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void controllerPointCut() {
    }


    @Before("controllerPointCut()")
    public void before(JoinPoint pjp) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            RequestInfoDto requestInfoDto = new RequestInfoDto();
            long beginTime = System.currentTimeMillis();
            UnicornContext.setValue("beginTime", beginTime);
            UnicornContext.setValue(UnicornConstants.REQUEST_TRACK_HEADER_NAME, request.getHeader(UnicornConstants.REQUEST_TRACK_HEADER_NAME));
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            String url = request.getRequestURL().toString();
            //设置请求ID
            requestInfoDto.setRequestId(request.getHeader(UnicornConstants.REQUEST_TRACK_HEADER_NAME));
            //请求方法
            requestInfoDto.setHttpMethod(request.getMethod());
            //请求url
            requestInfoDto.setRequestUrl(url);
            //请求IP
            requestInfoDto.setRemoteIp(RequestUtils.getIp(request));
            //请求控制器方法名
            requestInfoDto.setRequestMethod(signature.getDeclaringTypeName() + "." + signature.getName());
            // 请求参数
            Object args[] = pjp.getArgs();
            //请求报文
            for (Object arg : args) {
                if (arg instanceof Serializable) {

                    if (arg instanceof MultipartFile) {
                        requestInfoDto.getRequestBodys().add("流数据");
                        continue;
                    }
                    if (arg instanceof MultipartFile[]) {
                        requestInfoDto.getRequestBodys().add("[流数据]");
                        continue;
                    }
                    //请求报文
                    requestInfoDto.getRequestBodys().add(arg);
                }
            }
            //打印请求日志
            info("接口请求信息：{}", requestInfoDto);
        } catch (Exception e) {
            // ignore
        }

    }

    @AfterReturning(pointcut = "controllerPointCut()", returning = "ret")
    public void afterReturning(Object ret) {

        try {
            ResponseInfoDto responseInfoDto = new ResponseInfoDto();
            if (ret != null) {
                //设置响应ID
                responseInfoDto.setResponseId(UnicornContext.getValue(UnicornConstants.REQUEST_TRACK_HEADER_NAME));
                //设置响应报文
                responseInfoDto.setResponseBody(ret);
            }
            long costMs = System.currentTimeMillis() - Long.valueOf(UnicornContext.getValue("beginTime").toString());
            //设置请求开始时间
            responseInfoDto.setResponseTime(costMs + "ms");
            //打印响应日志
            info("接口响应信息：{}", responseInfoDto);
        } catch (Exception e) {

        }finally {
            UnicornContext.reset();
        }

    }

}
