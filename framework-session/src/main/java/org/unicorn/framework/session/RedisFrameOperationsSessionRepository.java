package org.unicorn.framework.session;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;

public class RedisFrameOperationsSessionRepository extends RedisOperationsSessionRepository {

	public RedisFrameOperationsSessionRepository(RedisConnectionFactory redisConnectionFactory) {
		super(redisConnectionFactory);
	}

}
