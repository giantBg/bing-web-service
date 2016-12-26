package com.bingcore.web.util.kafka.case3;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xubing on 16/10/17.
 */
public class KafakConsumer implements Runnable  {
    private ConsumerConfig consumerConfig;
    private static String topic="bing_cluster_log";
    Properties props;
    final int kafaPartitons = 3;

    public KafakConsumer() {
        props = new Properties();
        props.put("zookeeper.connect", "localhost:2181,localhost:2182,localhost:2183");
        props.put("group.id", "bing-group-001");
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");
        consumerConfig = new ConsumerConfig(props);
    }


    @Override
    public void run() {

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(kafaPartitons));
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        ExecutorService executor = Executors.newFixedThreadPool(kafaPartitons);
        for (final KafkaStream stream : streams) {
            executor.submit(new KafkaConsumerThread(stream));
        }

    }


    public static void main(String[] args) {
        System.out.println(topic);
        Thread t = new Thread(new KafakConsumer());
        t.start();
    }
}
