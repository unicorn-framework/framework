package org.unicorn.framework.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.unicorn.framework.base.base.AbstractService;
import org.unicorn.framework.cache.lock.LockService;

/**
 * @author  xiebin
 */
public abstract class AbstractLockService extends AbstractService {
    @Autowired
    private LockService lockService;

}
