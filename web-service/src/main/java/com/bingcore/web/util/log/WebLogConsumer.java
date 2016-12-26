package com.bingcore.web.util.log;

import com.bingcore.web.util.SysConfigUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.ishansong.log.Loggers;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xubing on 16/07/26.
 *
 * @Author xubing
 * @DateTime 2016-07-26
 * @Function kafka日志消费端
 */
public class WebLogConsumer {

    private static final Logger logger = Loggers.get("consumer");
    private static int threads = Runtime.getRuntime().availableProcessors() / 2 + 1;
    private static ExecutorService executor = Executors.newFixedThreadPool(threads);

    public static boolean shutdown = false;

    public static void start() {
        logger.info("===kafka log consumer exective====");
        Properties props = new Properties();
        props.put("zookeeper.connect", SysConfigUtil.ZK_SERVER);
        props.put("group.id", SysConfigUtil.KAFKA_CONSUMER_GROUP_ID);
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "2000");
        props.put("auto.commit.interval.ms", "1000");
        logger.info("zookeeper Properties={}", props.toString());
        final ConsumerConfig config = new ConsumerConfig(props);
        ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(SysConfigUtil.KAFKA_TOPIC, SysConfigUtil.KAFKA_TOPIC_NUM_PARTITIONS);//根据kafka里配置的partitions数设置

//        System.out.println(props.toString());
//        System.out.println("topic is:" + SysConfigUtil.KAFKA_TOPIC);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(SysConfigUtil.KAFKA_TOPIC);
//        System.out.println("log1 :" + streams.size() + ",flag:" + shutdown);
        for (final KafkaStream stream : streams) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        ConsumerIterator<byte[], byte[]> it = stream.iterator();
                        System.out.println("log2 :" + it.size());
                        while (it.hasNext()) {
                            //rebuild params
//                            System.out.println("log3 :" + it.next().message());
                            String logMessage = new String(it.next().message());
                            logger.info("consumer origin log: {}", logMessage);
//                            System.out.println("consumer origin log: " + logMessage);

                            if (!Strings.isNullOrEmpty(logMessage)) {
                                Integer quoteFlag = logMessage.indexOf(",") + 1;
                                String newMessage = logMessage.substring(quoteFlag);
                                Map<String, String> resultMaps = Splitter.on(",").trimResults().omitEmptyStrings().withKeyValueSeparator(":").split(newMessage);
                                logger.info("processed log info: {}", resultMaps);
                                String eventType = MapUtils.getString(resultMaps, "logType");
                                Preconditions.checkNotNull(eventType, "eventType is null !");

                                WebLogEvent logEvent = WebLogEvent.getEventByName(eventType);
                                if (logEvent != null) {
                                    logEvent.getHandler().exec(resultMaps);
                                }

                            }

                            if (shutdown) {
                                logger.info("system shutdown, stop consumer log...");
//                                System.out.println("system shutdown, stop consumer log...");
                                //退出
                                break;
                            }
                        }
                    } catch (Exception e) {
                        logger.error("failed to consumer kafka data log ({}),cause:{}", stream.toString(), Throwables.getStackTraceAsString(e));
                        e.printStackTrace();
                    }
                }
            });
        }
//        System.out.println("log4 :over");
    }

}
