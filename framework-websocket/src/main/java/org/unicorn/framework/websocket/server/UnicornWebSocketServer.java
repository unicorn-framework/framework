package org.unicorn.framework.websocket.server;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.unicorn.framework.cache.cache.CacheService;
import org.unicorn.framework.util.json.JsonUtils;
import org.unicorn.framework.web.base.SpringContextHolder;
import org.unicorn.framework.websocket.constants.Constants;
import org.unicorn.framework.websocket.mq.WebSocketMessage;
import org.unicorn.framework.websocket.mq.WebSocketProducer;
import org.unicorn.framework.websocket.utils.WebSocketSessionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author xiebin
 */
@ServerEndpoint("/ws/{sessionKey}")
@Component
@Slf4j
public class UnicornWebSocketServer {

    private CacheService cacheService;
    private WebSocketProducer webSocketProducer;

    /**
     * 连接建立成功调用的方法
     * 将session设置到消息消费者
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sessionKey") String sessionKey) {
        this.cacheService = SpringContextHolder.getBean(CacheService.class);
        this.webSocketProducer = SpringContextHolder.getBean(WebSocketProducer.class);
        Set<String> userSet = addWebSocketQueue(sessionKey);
        WebSocketSessionUtils.putWebSocketSession(sessionKey, session);
        log.info("用户连接:" + sessionKey + ",当前在线人数为:" + userSet.size());
        try {
            sendMessage(session, "连接成功");
        } catch (IOException e) {
            log.error("用户:" + sessionKey + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("sessionKey") String sessionKey) {
        Set<String> userSet = delWebSocketQueue(sessionKey);
        WebSocketSessionUtils.delWebSocketSession(sessionKey);
        log.info("用户退出:" + sessionKey + ",当前在线人数为:" + userSet.size());
    }

    private Set<String> addWebSocketQueue(@PathParam("sessionKey") String sessionKey) {
        Set<String> userSet = getOnlineUserSet();
        userSet.add(sessionKey);
        cacheService.put(Constants.WEBSOCKET_SESSION_KEY, userSet, -1, TimeUnit.SECONDS, Constants.WEBSOCKET_NAMESPACE);
        return userSet;
    }

    private Set<String> delWebSocketQueue(@PathParam("sessionKey") String sessionKey) {
        Set<String> userSet = getOnlineUserSet();
        userSet.remove(sessionKey);
        cacheService.put(Constants.WEBSOCKET_SESSION_KEY, userSet, -1, TimeUnit.SECONDS, Constants.WEBSOCKET_NAMESPACE);
        return userSet;
    }


    /**
     * @return
     */
    private Set<String> getOnlineUserSet() {
        Set<String> userSet = Sets.newHashSet();
        //如果包含
        if (cacheService.containsKey(Constants.WEBSOCKET_SESSION_KEY, Constants.WEBSOCKET_NAMESPACE)) {
            userSet = cacheService.get(Constants.WEBSOCKET_SESSION_KEY, Constants.WEBSOCKET_NAMESPACE, Set.class);
        }
        return userSet;
    }


    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("sessionKey") String sessionKey) throws Exception {
        log.info("用户消息:" + sessionKey + ",报文:" + message);
        WebSocketMessage webSocketMessage = JsonUtils.fromJson(message, WebSocketMessage.class);
        //将消息发送到消息队列
        webSocketProducer.sendWebscoketMessage(webSocketMessage);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, @PathParam("sessionKey") String sessionKey, Throwable error) {
        log.error("用户错误:" + sessionKey + ",原因:" + error.getMessage());
        delWebSocketQueue(sessionKey);
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }
}