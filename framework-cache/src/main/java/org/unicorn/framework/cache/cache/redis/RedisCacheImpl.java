package org.unicorn.framework.cache.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.unicorn.framework.cache.cache.CacheService;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xiebin
 */
@Component("cacheService")
@Slf4j
public class RedisCacheImpl implements CacheService {
    @Autowired
    @Qualifier("objectRedisTemplate")
    private RedisTemplate objectRedisTemplate;

    @Override
    public void put(String key, Object value, int timeToLive, TimeUnit timeUnit, String namespace) {
        put(key, value, -1, timeToLive, timeUnit, namespace);
    }

    @Override
    public void put(String key, Object value, int timeToIdle, int timeToLive, TimeUnit timeUnit, String namespace) {
        if (key == null) {
            return;
        }
        try {
            if (timeToLive > 0) {
                objectRedisTemplate.opsForValue().set(generateKey(key, namespace), value, timeToLive, timeUnit);
            } else {
                objectRedisTemplate.opsForValue().set(generateKey(key, namespace), value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(String key, String namespace) {
        if (key == null) {
            return false;
        }
        try {
            return objectRedisTemplate.hasKey(generateKey(key, namespace));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object get(String key, String namespace) {
        if (key == null) {
            return null;
        }
        try {
            return objectRedisTemplate.opsForValue().get(generateKey(key, namespace));
        } catch (SerializationFailedException e) {
            log.warn(e.getMessage(), e);
            delete(key, namespace);
            return null;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }


    @Override
    public <T> T get(String key, String namespace, Class<T> clazz) {
        return (T) get(key, namespace);
    }

    @Override
    public Object get(String key, String namespace, int timeToIdle, TimeUnit timeUnit) {
        if (key == null) {
            return null;
        }
        String actualKey = generateKey(key, namespace);
        if (timeToIdle > 0) {
            objectRedisTemplate.expire(actualKey, timeToIdle, timeUnit);
        }
        try {
            return objectRedisTemplate.opsForValue().get(actualKey);
        } catch (SerializationFailedException e) {
            log.warn(e.getMessage(), e);
            delete(key, namespace);
            return null;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public <T> T get(String key, String namespace, int timeToIdle, TimeUnit timeUnit, Class<T> clazz) {
        return (T) get(key, namespace, timeToIdle, timeUnit);
    }

    @Override
    public void delete(String key, String namespace) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        try {
            objectRedisTemplate.delete(generateKey(key, namespace));
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void mput(Map<String, ?> map, final int timeToLive, TimeUnit timeUnit, String namespace) {
        if (map == null) {
            return;
        }
        try {
            final Map<byte[], byte[]> actualMap = new HashMap<>();
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                actualMap.put(objectRedisTemplate.getKeySerializer().serialize(generateKey(entry.getKey(), namespace)),
                        objectRedisTemplate.getValueSerializer().serialize(entry.getValue()));
            }
            objectRedisTemplate.execute(conn -> {
                conn.multi();
                try {
                    conn.mSet(actualMap);
                    if (timeToLive > 0) {
                        for (byte[] k : actualMap.keySet()) {
                            conn.expire(k, timeToLive);
                        }
                    }
                    conn.exec();
                } catch (Exception e) {
                    conn.discard();
                }
                return null;

            }, objectRedisTemplate.isExposeConnection());
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public <T> Map<String, T> mget(Collection<String> keys, String namespace) {
        if (keys == null) {
            return null;
        }
        final List<byte[]> _keys = new ArrayList<>();
        for (String key : keys) {
            _keys.add(objectRedisTemplate.getKeySerializer().serialize(generateKey(key, namespace)));
        }
        try {
            List<byte[]> values = (List<byte[]>) objectRedisTemplate.execute(conn -> {
                return conn.mGet(_keys.toArray(new byte[0][0]));
            }, objectRedisTemplate.isExposeConnection());
            Map<String, T> map = new HashMap<>();
            int i = 0;
            for (String key : keys) {
                map.put(key, (T) objectRedisTemplate.getValueSerializer().deserialize(values.get(i)));
                i++;
            }
            return map;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void mdelete(final Collection<String> keys, final String namespace) {
        if (keys == null) {
            return;
        }
        try {
            objectRedisTemplate.execute(conn -> {
                conn.multi();
                try {
                    for (String key : keys) {
                        if (StringUtils.isNotBlank(key)) {
                            conn.del(objectRedisTemplate.getKeySerializer().serialize(generateKey(key, namespace)));
                        }
                        conn.exec();
                    }
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                    conn.discard();
                }
                return null;
            }, objectRedisTemplate.isExposeConnection());
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public boolean containsKey(String key, String namespace) {
        if (key == null) {
            return false;
        }
        try {
            return objectRedisTemplate.hasKey(generateKey(key, namespace));
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean putIfAbsent(String key, Object value, int timeToLive, TimeUnit timeUnit, String namespace) {
        try {
            String actrualkey = generateKey(key, namespace);
            boolean success = objectRedisTemplate.opsForValue().setIfAbsent(actrualkey, value);
            if (success && timeToLive > 0) {
                objectRedisTemplate.expire(actrualkey, timeToLive, timeUnit);
            }
            return success;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean putIfAbsent(String key, Object value, Date expireDate, String namespace) {
        try {
            String actrualkey = generateKey(key, namespace);
            boolean success = objectRedisTemplate.opsForValue().setIfAbsent(actrualkey, value);
            if (success && expireDate != null) {
                objectRedisTemplate.expireAt(actrualkey, expireDate);
            }
            return success;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public long increment(String key, long delta, String namespace) {
        return increment(key, delta, 0, null, namespace);
    }


    @Override
    public long increment(String key, long delta, int timeToLive, TimeUnit timeUnit, String namespace) {
        try {
            String actrualkey = generateKey(key, namespace);
            long result = objectRedisTemplate.opsForValue().increment(actrualkey, delta);
            if (timeToLive > 0) {
                objectRedisTemplate.expire(actrualkey, timeToLive, timeUnit);
            }
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

}
