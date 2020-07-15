package org.unicorn.framework.websocket.mq;

import lombok.*;

/**
 * @author xiebin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WebSocketMessage<T> {
    /**
     * websocket链接key
     */
    private String sessionKey;
    /**
     * websocket要传递的消息对象
     */
    private T message;
}
