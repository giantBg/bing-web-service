package com.bingcore.web.util;

import com.ishansong.common.config.Config;
import com.ishansong.common.config.ConfigCenter;

/**
 * Created by xubing on 16/07/26.
 *
 * @Author xubing
 * @DateTime 2016-07-26
 * @Function 系统配置获取工具类
 */
public class SysConfigUtil {

    /**
     * kafka配置
     */
    public static String ZK_SERVER;
    /**
     * kafka consumer group id
     */
    public static String KAFKA_CONSUMER_GROUP_ID;
    /**
     * kafka topic
     */
    public static String KAFKA_TOPIC;
    /**
     * kafka topic partitions
     */
    public static Integer KAFKA_TOPIC_NUM_PARTITIONS;

    private static Config config;

    static {
        config = ConfigCenter.getConfig("app.properties");

        //get kafka config
        ZK_SERVER = config.getString("zk.server");
        KAFKA_CONSUMER_GROUP_ID = config.getString("kafka.group.id");
        KAFKA_TOPIC = config.getString("kafka.topic");
        KAFKA_TOPIC_NUM_PARTITIONS = config.getInt("kafka.num.partitions", 1);
    }

}
