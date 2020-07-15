package org.unicorn.framework.websocket.constants;

import java.io.Serializable;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2020-07-15 9:36
 */
public class Constants implements Serializable {
    /**
     * websocket namespace
     */
    public static final String WEBSOCKET_NAMESPACE = "webcoket:session";

    /**
     * websocket session count
     */
    public static final String WEBSOCKET_SESSION_KEY = "webcoket:session:count:";
}
