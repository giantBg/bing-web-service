package com.bingcore.web.util.log;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.ishansong.log.Loggers;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xubing on 16/8/3.
 */
public class kafkaConsumer extends Thread {

    private static final Logger logger = Loggers.get("consumer");

    private String topic;

    public kafkaConsumer(String topic) {
        super();
        this.topic = topic;
    }


    @Override
    public void run() {
        ConsumerConnector consumer = createConsumer();
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1); // 一次从主题中获取一个数据
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = messageStreams.get(topic).get(0);// 获取每次接收到的这个数据
        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
        while (iterator.hasNext()) {
            String message = new String(iterator.next().message());
            logger.info("接收到: " + message);

            //business deal
            String logMessage = message;
            if (!Strings.isNullOrEmpty(logMessage)) {
                Integer quoteFlag = logMessage.indexOf(",") + 1;
                if (quoteFlag > 0) {
                    String newMessage = logMessage.substring(quoteFlag);
                    Map<String, String> resultMaps = Splitter.on(",").trimResults().omitEmptyStrings().withKeyValueSeparator(":").split(newMessage);
                    logger.info("processed log info: {}", resultMaps);
                    String eventType = MapUtils.getString(resultMaps, "logType");
                    if (Strings.isNullOrEmpty(eventType)) {
                        logger.error("eventType is null, message:{}", logMessage);
                    } else {
                        WebLogEvent logEvent = WebLogEvent.getEventByName(eventType);
                        if (logEvent != null) {
                            logEvent.getHandler().exec(resultMaps);
                        } else {
                            logger.error("event handler is null for {}", eventType);
                        }
                    }
                }
            }
        }
    }

    private ConsumerConnector createConsumer() {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "127.0.0.1:2181");//声明zk
        properties.put("group.id", "group2");// 必须要使用别的组名称， 如果生产者和消费者都在同一组，则不能访问同一组内的topic数据
        return Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
    }


    public static void main(String[] args) {
        new kafkaConsumer("dev_stat_log").start();// 使用kafka集群中创建好的主题 test

    }

}
