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
    private String sessionKey;
    private T message;
}
