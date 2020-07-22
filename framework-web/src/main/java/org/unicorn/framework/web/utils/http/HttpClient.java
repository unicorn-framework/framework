package org.unicorn.framework.web.utils.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.http.impl.client.HttpClients.createDefault;

/**
 * 发送Http请求工具类
 */
public class HttpClient {

    private static final int DEFAULT_TIMEOUT = 10000;
    private static final Log logger = LogFactory.getLog(HttpClient.class);

    /**
     * post 方法
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params) throws IOException {
        if (StringUtils.isEmpty(url) || params == null || params.isEmpty()) {
            return "";
        }

        CloseableHttpClient httpClient = createDefault();
        CloseableHttpResponse response = null;
        String result = null;

        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig
                    .custom()
                    .setSocketTimeout(DEFAULT_TIMEOUT)
                    .setConnectTimeout(DEFAULT_TIMEOUT)
                    .build();//设置请求和传输超时时间

            httpPost.setConfig(requestConfig);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
            for (Map.Entry<String, String> entity : params.entrySet()) {
                basicNameValuePairs.add(new BasicNameValuePair(entity.getKey(), entity.getValue()));
            }

            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(basicNameValuePairs, Consts.UTF_8);
            httpPost.setEntity(urlEncodedFormEntity);

            response = httpClient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            logger.info(String.format("request url: %s, response status: %s",
                    url, statusLine.getStatusCode()));

            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, Consts.UTF_8);

            return result == null ? "" : result.trim();

        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error("close http client failed", e);
            }
        }

    }

}

