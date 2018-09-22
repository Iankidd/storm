package org.storm.service.redis.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

public abstract class BaseRedisService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Jedis borrowJedis(JedisPool jedisPool) {
        return jedisPool.getResource();
    }

    public void close(JedisPool jedisPool) {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    public interface RedisCaller {
        Object doWith(Jedis jedis);
    }

    /**
     * redis交互封装，调用者通过实现RedisCaller，实现对redis的具体操作
     *
     * @param caller
     * @return
     */
    public Object process(JedisPool jedisPool, RedisCaller caller) {
        Jedis jedis = borrowJedis(jedisPool);
        try {
            return caller.doWith(jedis);
        } catch (JedisConnectionException e) {
            logger.error(e.toString());
            throw e;
        } finally {
            close(jedisPool);
        }
    }
}
