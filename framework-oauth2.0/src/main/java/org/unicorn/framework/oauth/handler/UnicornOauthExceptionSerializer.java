package org.unicorn.framework.oauth.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.unicorn.framework.core.SysCode;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 * @author xiebin
 * @since 1.0
 */
public class UnicornOauthExceptionSerializer extends StdSerializer<UnicornOauthException> {
    public UnicornOauthExceptionSerializer() {
        super(UnicornOauthException.class);
    }

    @Override
    public void serialize(UnicornOauthException value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        gen.writeStartObject();
        gen.writeStringField("resCode", SysCode.SYS_FAIL.getCode());
        gen.writeStringField("resInfo", value.getMessage());
        gen.writeStringField("url", request.getServletPath());
        gen.writeStringField("data", null);
        gen.writeEndObject();
    }
}
