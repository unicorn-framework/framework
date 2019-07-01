package org.unicorn.framework.core.exceptionhandler;

import org.unicorn.framework.core.ResponseDto;

/**
 * @author  xiebin
 */
public interface IExceptionHandler {
    /**
     * 是否支持该操作
     * @param e
     * @return
     */
     boolean  supports(Exception e);

    /**
     * 具体的处理逻辑
     * @param e
     * @param url 请求URL
     * @return
     */
    ResponseDto<String> handler(Exception e,String url);

}
