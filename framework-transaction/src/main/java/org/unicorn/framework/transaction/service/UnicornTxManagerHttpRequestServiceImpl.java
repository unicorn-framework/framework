package org.unicorn.framework.transaction.service;

import com.codingapi.tx.netty.service.TxManagerHttpRequestService;
import com.lorne.core.framework.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xiebin
 */
@Service
@Slf4j
public class UnicornTxManagerHttpRequestServiceImpl implements TxManagerHttpRequestService {

    @Override
    public String httpGet(String url) {
        log.info("httpGet-start");
        String res = HttpUtils.get(url);
        log.info("httpGet-end");
        return res;
    }

    @Override
    public String httpPost(String url, String params) {
        log.info("httpPost-start");
        String res = HttpUtils.post(url,params);
        log.info("httpPost-end");
        return res;
    }
}