
package org.unicorn.framework.web.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.assertj.core.util.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.unicorn.framework.base.base.AbstractService;
import org.unicorn.framework.base.base.UnicornContext;
import org.unicorn.framework.base.constants.UnicornConstants;
import org.unicorn.framework.core.dto.RequestInfoDto;
import org.unicorn.framework.core.dto.ResponseInfoDto;
import org.unicorn.framework.web.utils.http.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiebin
 */
@Aspect
@Component
@Slf4j
@ConditionalOnProperty(prefix = "unicorn.api.log", name = "enable", havingValue = "true")
public class LogAspect extends AbstractService {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void controllerPointCut() {
    }


    @Around("controllerPointCut()")
    public Object processTx(ProceedingJoinPoint jp) throws Throwable {
        //请求开始时间
        long beginTime = System.currentTimeMillis();
        //获取请求参数
        Object[] args = jp.getArgs();
        //请求之前
        doBefore(jp, args);
        //调用目标方法
        Object result = jp.proceed(args);
        //请求之后
        doAfter(result, beginTime);
        return result;
    }


    public void doBefore(JoinPoint pjp, Object args[]) {
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            boolean flag = signature.getDeclaringType().isAnnotationPresent(FeignClient.class);
            if (flag) {
                return;
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            RequestInfoDto requestInfoDto = new RequestInfoDto();
            UnicornContext.setValue(UnicornConstants.REQUEST_TRACK_HEADER_NAME, request.getHeader(UnicornConstants.REQUEST_TRACK_HEADER_NAME));
            //获取请求URL
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
            //设置请求头
            requestInfoDto.setHeaders(getHeaders(request));
            //设置请求报文
            requestInfoDto.setRequestBodys(getRequestBody(args));
            //打印请求日志
            info("接口请求信息：{}", requestInfoDto);
        } catch (Exception e) {
            log.warn("请求前置拦截失败");
        }

    }


    public void doAfter(Object ret, long beginTime) {
        try {
            ResponseInfoDto responseInfoDto = new ResponseInfoDto();
            if (ret != null) {
                //设置响应ID
                responseInfoDto.setResponseId(UnicornContext.getValue(UnicornConstants.REQUEST_TRACK_HEADER_NAME));
                if (ret instanceof ResponseEntity) {
                    ResponseEntity entity = (ResponseEntity) ret;
                    //设置响应报文
                    responseInfoDto.setResponseBody(entity.getBody());
                } else {
                    //设置响应报文
                    responseInfoDto.setResponseBody(ret);
                }
            }
            long costMs = System.currentTimeMillis() - beginTime;
            //设置请求开始时间
            responseInfoDto.setResponseTime(costMs + "ms");
            //打印响应日志
            info("接口响应信息：{}", responseInfoDto);
        } catch (Exception e) {
            log.warn("请求后置拦截失败");
        } finally {
            UnicornContext.reset();
        }

    }


    /**
     * 获取请求报文
     *
     * @param args
     * @return
     */
    private List<Object> getRequestBody(Object args[]) {
        List<Object> requestBodys = Lists.newArrayList();
        for (Object arg : args) {
            if (arg instanceof Serializable) {

                if (arg instanceof MultipartFile) {
                    requestBodys.add("流数据");
                    continue;
                }
                if (arg instanceof MultipartFile[]) {
                    requestBodys.add("[流数据]");
                    continue;
                }
                //请求报文
                requestBodys.add(arg);
            }
        }
        return requestBodys;
    }

    /**
     * 获取请求头
     *
     * @param request
     * @return
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headerMaps = new HashMap<>();
        for (Enumeration<String> enu = request.getHeaderNames(); enu.hasMoreElements(); ) {
            String name = enu.nextElement();
            headerMaps.put(name, request.getHeader(name));
        }
        return headerMaps;
    }

}
