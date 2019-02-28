package org.unicorn.framework.api.doc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.unicorn.framework.api.doc.controller.CustomSwagger2Controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiebin
 */
public class RequestUtils {

    private RequestUtils() {
    }

    public static boolean SWAGGER_IS_LOGIN = false;

    private static final Logger logger = LoggerFactory.getLogger(CustomSwagger2Controller.class);

    public static void writeForbidden(HttpServletResponse response) throws IOException {
        final HttpStatus status = HttpStatus.UNAUTHORIZED;
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        ServletOutputStream outputStream = response.getOutputStream();
        String error = "{\"code\":%d,\"message\":\"%s\"}";
        final String format = String.format(error, status.value(), status.getReasonPhrase());
        logger.error(format);
        outputStream.write(format.getBytes());
        outputStream.flush();
        outputStream.close();
    }


}
