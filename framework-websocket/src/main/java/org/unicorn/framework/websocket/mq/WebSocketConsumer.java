package org.unicorn.framework.websocket.mq;

import lombok.extern.slf4j.Slf4j;
import org.unicorn.framework.mq.annotation.MQConsumer;
import org.unicorn.framework.mq.base.AbstractMQPushConsumer;
import org.unicorn.framework.mq.base.MessageExtConst;
import org.unicorn.framework.util.json.JsonUtils;
import org.unicorn.framework.websocket.utils.WebSocketSessionUtils;

import java.util.Map;

/**
 * websocket广播消费模式
 *
 * @author xiebin
 * @since 2020/01/18
 */
@Slf4j
@MQConsumer(consumerGroup = "websocket_group", topic = "unicorn", tag = "websocket", messageMode = MessageExtConst.MESSAGE_MODE_BROADCASTING)
public class WebSocketConsumer extends AbstractMQPushConsumer<WebSocketMessage> {

    @Override
    public boolean process(WebSocketMessage message, Map<String, Object> map) {
        log.info("webSocket消息:{}", JsonUtils.toJson(message));
        try {
            if (WebSocketSessionUtils.getWebSocketSession(message.getSessionKey()) != null) {
                WebSocketSessionUtils.getWebSocketSession(message.getSessionKey()).getBasicRemote().sendText(JsonUtils.toJson(message));
            }
        } catch (Exception e) {
            log.error("websocket消息失败", e);
        }
        return true;
    }
}
