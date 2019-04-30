package org.unicorn.framework.transaction.service;

import com.codingapi.tx.config.service.TxManagerTxUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author xiebin
 */
@Service
@Slf4j
public class UnicornTxManagerTxUrlServiceImpl implements TxManagerTxUrlService {


    @Value("${unicorn.tx.manager.url}")
    private String url;

    @Override
    public String getTxUrl() {
        log.info("load tm.manager.url ");
        return url;
    }
}