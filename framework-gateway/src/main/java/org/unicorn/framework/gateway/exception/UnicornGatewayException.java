package org.unicorn.framework.gateway.exception;

import com.netflix.zuul.exception.ZuulException;
import lombok.Data;
import org.apache.http.HttpStatus;
import org.unicorn.framework.core.exception.PendingException;

/**
 * @author xiebin
 */
@Data
public class UnicornGatewayException extends ZuulException {
    private PendingException pe;

    public UnicornGatewayException(PendingException pe) {

        super(pe.getCause(), pe.getMessage(), HttpStatus.SC_OK, pe.getCode());
        this.pe = pe;
    }
}
