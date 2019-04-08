package org.unicorn.framework.core.fallback;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.util.json.JsonUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 
 * @author xiebin
 *
 */
public abstract class BaseServiceConsumerFallbackProvider implements FallbackProvider {

	@Override
	public abstract String getRoute() ;



	@Override
	public ClientHttpResponse fallbackResponse(Throwable cause) {
		 return new ClientHttpResponse() {
	            @Override
	            public HttpStatus getStatusCode() throws IOException {
	                return HttpStatus.OK;
	            }
	 
	            @Override
	            public int getRawStatusCode() throws IOException {
	                return this.getStatusCode().value();
	            }
	 
	            @Override
	            public String getStatusText() throws IOException {
	                return this.getStatusCode().getReasonPhrase();
	            }
	 
	            @Override
	            public void close() {
	 
	            }
	 
	            @Override
	            public InputStream getBody() throws IOException {
	            	ResponseDto<?> dto=new ResponseDto<>(SysCode.MICRO_SERVICE_ERROR);
	                return new ByteArrayInputStream(JsonUtils.toJson(dto).getBytes());
	            }
	 
	            @Override
	            public HttpHeaders getHeaders() {
	                HttpHeaders headers = new HttpHeaders();
	                MediaType mt = new MediaType("application", "json", Charset.forName("UTF-8"));
	                headers.setContentType(mt);
	                return headers;
	            }
	        };
	}

}
