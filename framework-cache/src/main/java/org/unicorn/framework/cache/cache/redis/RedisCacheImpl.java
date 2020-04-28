package org.unicorn.framework.cache.cache.redis;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.unicorn.framework.cache.cache.CacheService;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Component("cacheService")
public class RedisCacheImpl  implements CacheService {

	private RedisTemplate redisTemplate;

	private RedisTemplate stringRedisTemplate;

	@Autowired
	public RedisCacheImpl(@Qualifier("redisTemplate") RedisTemplate redisTemplate,
			@Qualifier("stringRedisTemplate") RedisTemplate stringRedisTemplate) {
		this.redisTemplate = redisTemplate;
		this.stringRedisTemplate = stringRedisTemplate;
	}

	@Override
	public void put(String key, Object value, int timeToLive, TimeUnit timeUnit, String namespace) {
		put(key, value, -1, timeToLive, timeUnit, namespace);
	}

	@Override
	public void put(String key, Object value, int timeToIdle, int timeToLive, TimeUnit timeUnit, String namespace) {
		if (key == null)
			return;
		try {
			if (timeToLive > 0)
				redisTemplate.opsForValue().set(generateKey(key, namespace), value, timeToLive, timeUnit);
			else
				redisTemplate.opsForValue().set(generateKey(key, namespace), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean exists(String key, String namespace) {
		if (key == null)
			return false;
		try {
			return redisTemplate.hasKey(generateKey(key, namespace));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Object get(String key, String namespace) {
		if (key == null)
			return null;
		try {
			return redisTemplate.opsForValue().get(generateKey(key, namespace));
		} catch (SerializationFailedException e) {
//			warn(e.getMessage(), e);
			delete(key, namespace);
			return null;
		} catch (Exception e) {
//			warn(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public Object get(String key, String namespace, int timeToIdle, TimeUnit timeUnit) {
		if (key == null)
			return null;
		String actualKey = generateKey(key, namespace);
		if (timeToIdle > 0)
			redisTemplate.expire(actualKey, timeToIdle, timeUnit);
		try {
			return redisTemplate.opsForValue().get(actualKey);
		} catch (SerializationFailedException e) {
//			warn(e.getMessage(), e);
			delete(key, namespace);
			return null;
		} catch (Exception e) {
//			warn(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void delete(String key, String namespace) {
		if (StringUtils.isBlank(key))
			return;
		try {
			redisTemplate.delete(generateKey(key, namespace));
		} catch (Exception e) {
//			warn(e.getMessage(), e);
		}
	}

	@Override
	public void mput(Map<String, Object> map, final int timeToLive, TimeUnit timeUnit, String namespace) {
		if (map == null)
			return;
		try {
			final Map<byte[], byte[]> actualMap = new HashMap<byte[], byte[]>();
			for (Map.Entry<String, Object> entry : map.entrySet())
				actualMap.put(redisTemplate.getKeySerializer().serialize(generateKey(entry.getKey(), namespace)),
						redisTemplate.getValueSerializer().serialize(entry.getValue()));
			redisTemplate.execute(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection conn) throws DataAccessException {
					conn.multi();
					try {
						conn.mSet(actualMap);
						if (timeToLive > 0)
							for (byte[] k : actualMap.keySet())
								conn.expire(k, timeToLive);
						conn.exec();
					} catch (Exception e) {
						conn.discard();
					}
					return null;
				}
			});

		} catch (Exception e) {
//			warn(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Object> mget(Collection<String> keys, String namespace) {
		if (keys == null)
			return null;
		final List<byte[]> _keys = new ArrayList<byte[]>();
		for (String key : keys)
			_keys.add(redisTemplate.getKeySerializer().serialize(generateKey(key, namespace)));
		try {
			List<byte[]> values = (List<byte[]>) redisTemplate.execute(new RedisCallback<List<byte[]>>() {
				@Override
				public List<byte[]> doInRedis(RedisConnection conn) throws DataAccessException {
					return conn.mGet(_keys.toArray(new byte[0][0]));
				}
			});
			Map<String, Object> map = new HashMap<String, Object>();
			int i = 0;
			for (String key : keys) {
				map.put(key, redisTemplate.getValueSerializer().deserialize(values.get(i)));
				i++;
			}
			return map;
		} catch (Exception e) {
//			warn(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void mdelete(final Collection<String> keys, final String namespace) {
		if (keys == null)
			return;
		try {
			redisTemplate.execute(new RedisCallback() {
				@Override
				public Object doInRedis(RedisConnection conn) throws DataAccessException {
					conn.multi();
					try {
						for (String key : keys)
							if (StringUtils.isNotBlank(key))
								conn.del(redisTemplate.getKeySerializer().serialize(generateKey(key, namespace)));
						conn.exec();
					} catch (Exception e) {
//						warn(e.getMessage(), e);
						conn.discard();
					}
					return null;
				}
			});
		} catch (Exception e) {
//			warn(e.getMessage(), e);
		}
	}

	@Override
	public boolean containsKey(String key, String namespace) {
		if (key == null)
			return false;
		try {
			return redisTemplate.hasKey(generateKey(key, namespace));
		} catch (Exception e) {
//			warn(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean putIfAbsent(String key, Object value, int timeToLive, TimeUnit timeUnit, String namespace) {
		try {
			String actrualkey = generateKey(key, namespace);
			boolean success = redisTemplate.opsForValue().setIfAbsent(actrualkey, value);
			if (success && timeToLive > 0)
				redisTemplate.expire(actrualkey, timeToLive, timeUnit);
			return success;
		} catch (Exception e) {
			return false;
		}
	}
	@Override
	public boolean putIfAbsent(String key, Object value, Date expireDate, String namespace) {
		try {
			String actrualkey = generateKey(key, namespace);
			boolean success = redisTemplate.opsForValue().setIfAbsent(actrualkey, value);
			if (success && expireDate!=null)
				redisTemplate.expireAt(actrualkey, expireDate);
			return success;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public long increment(String key, long delta, int timeToLive, TimeUnit timeUnit, String namespace) {
		try {
			String actrualkey = generateKey(key, namespace);
			long result = redisTemplate.opsForValue().increment(actrualkey, delta);
			if (timeToLive > 0)
				redisTemplate.expire(actrualkey, timeToLive, timeUnit);
			return result;
		} catch (Exception e) {
			return -1;
		}
	}

	private String generateKey(String key, String namespace) {
		if (StringUtils.isNotBlank(namespace)) {
			StringBuilder sb = new StringBuilder(namespace.length() + key.length() + 1);
			sb.append(namespace);
			sb.append(':');
			sb.append(key);
			return sb.toString();
		} else {
			return key;
		}

	}

	@Override
	public boolean supportsTimeToIdle() {
		return false;
	}

	@Override
	public boolean supportsUpdateTimeToLive() {
		return true;
	}

	@Override
	public void invalidate(String namespace) {
		RedisScript<Boolean> script = new DefaultRedisScript<>(
				"local keys = redis.call('keys', ARGV[1]) \n for i=1,#keys,5000 do \n redis.call('del', unpack(keys, i, math.min(i+4999, #keys))) \n end \n return true",
				Boolean.class);
		stringRedisTemplate.execute(script, null, namespace + ":*");
	}

}
