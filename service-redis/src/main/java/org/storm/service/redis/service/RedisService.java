package org.storm.service.redis.service;

import java.util.Set;

public interface RedisService {

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys);

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern);

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key);

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key);

    /**
     * 获取所有的key
     *
     * @return
     */
    public Set<Object> keys();

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value);

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime);
}
