package com.bingcore.web.util.kafka.case2;

import com.google.common.base.Throwables;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by xubing on 16/07/26.
 *
 * @Author xubing
 * @DateTime 2016-07-26
 * @Function kafka日志消费端
 */
public class WebKafkaConsumer {

    private static int threads = Runtime.getRuntime().availableProcessors() / 2 + 1;
    private static ExecutorService executor = Executors.newFixedThreadPool(threads);
    public static boolean shutdown = false;

    public static void main(String[] args) {

        System.out.println("===kafka log consumer exective====");
        Properties props = new Properties();
        props.put("zookeeper.connect", "127.0.0.1:2181");
        props.put("group.id", "dev_test_group1");
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "2000");
        props.put("auto.commit.interval.ms", "1000");
        System.out.println("zookeeper Properties=" + props.toString());
        final ConsumerConfig config = new ConsumerConfig(props);
        final ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put("dev_stat_log", 1);//根据kafka里配置的partitions数设置

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get("dev_stat_log");
        System.out.println("consumer size: " + streams.size());
        for (final KafkaStream stream : streams) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        ConsumerIterator<byte[], byte[]> it = stream.iterator();
                        while (it.hasNext()) {
                            //rebuild params
                            String logMessage = new String(it.next().message());
                            System.out.println("consumer origin log: " + logMessage);


                            if (shutdown) {
                                if (consumer != null) {
                                    consumer.shutdown();
                                }
                                if (executor != null) {
                                    executor.shutdown();
                                }
                                try {
                                    if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                                        System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
                                    }
                                } catch (InterruptedException e) {
                                    System.out.println("Interrupted during shutdown, exiting uncleanly");
                                }
                                System.out.println("system shutdown, stop consumer log...");
                                //退出
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("failed to consumer kafka data :" + Throwables.getStackTraceAsString(e));
                        e.printStackTrace();
                    }
                }
            });
        }
        System.out.println("consumer over: ");
    }
}
