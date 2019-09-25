package com.dotwait.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * 跟踪工具
 */
public class TraceUtil {
    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(TraceUtil.class);
    /**
     * 记录开始时间
     */
    private long startTime;
    /**
     * 记录当前时间
     */
    private long currentTime;
    /**
     * 标识trace的唯一性
     */
    private UUID uuid;

    /**
     * 初始化uuid和开始时间
     */
    public TraceUtil() {
        startTime = System.currentTimeMillis();
        uuid = UUID.randomUUID();
        LOG.debug("UUID ==> {}, start trace", uuid.toString());
    }

    /**
     * 打印从开始到当前时间点所消耗的时间
     *
     * @param message 当前时间点的标识信息
     */
    public void stopAndWatch(String message) {
        currentTime = System.currentTimeMillis();
        LOG.debug("UUID ==> {}, from start to {}, use time {} ms.", uuid.toString(), message, currentTime - startTime);
    }
}
