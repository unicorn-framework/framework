package org.unicorn.framework.websocket.utils;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2020-07-15 14:48
 */
public class WebSocketSessionUtils {
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
     */
    private static ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 根据sessionKey获取 session对象
     *
     * @param sessionKey
     * @return
     */
    public static Session getWebSocketSession(String sessionKey) {
        return webSocketMap.get(sessionKey);
    }

    /**
     * 存储 session
     *
     * @param sessionKey
     * @param session
     */
    public static void putWebSocketSession(String sessionKey, Session session) {
        if (webSocketMap.containsKey(sessionKey)) {
            webSocketMap.remove(sessionKey);
            webSocketMap.put(sessionKey, session);
        } else {
            webSocketMap.put(sessionKey, session);
        }
    }


    /**
     * 删除 session
     *
     * @param sessionKey
     */
    public static void delWebSocketSession(String sessionKey) {
        if (webSocketMap.containsKey(sessionKey)) {
            webSocketMap.remove(sessionKey);
        }
    }

    /**
     * 删除 session
     *
     * @param sessionKey
     */
    public static boolean containsKey(String sessionKey) {
        return webSocketMap.containsKey(sessionKey);
    }

}
