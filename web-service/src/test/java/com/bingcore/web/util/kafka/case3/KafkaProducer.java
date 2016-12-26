package com.bingcore.web.util.kafka.case3;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KafkaProducer implements Runnable {

    private Producer<String, String> producer = null;

    private ProducerConfig config = null;

    final int kafaPartitons = 3;
    private static String topic = "bing_cluster_log";

    public KafkaProducer() {
        Properties props = new Properties();
        props.put("zookeeper.connect", "localhost:2181,localhost:2182,localhost:2183");

        // 指定序列化处理类，默认为kafka.serializer.DefaultEncoder,即byte[]
        props.put("serializer.class", "kafka.serializer.StringEncoder");

        // 同步还是异步，默认2表同步，1表异步。异步可以提高发送吞吐量，但是也可能导致丢失未发送过去的消息  
        props.put("producer.type", "sync");

        // 是否压缩，默认0表示不压缩，1表示用gzip压缩，2表示用snappy压缩。压缩后消息中会有头来指明消息压缩类型，故在消费者端消息解压是透明的无需指定。  
        props.put("compression.codec", "1");

        // 指定kafka节点列表，用于获取metadata(元数据)，不必全部指定  
        props.put("metadata.broker.list", "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094");

        config = new ProducerConfig(props);
    }

    @Override
    public void run() {
        producer = new Producer<String, String>(config);


        for (int i = 1; i <=kafaPartitons; i++) { //往3个分区发数据
            List<KeyedMessage<String, String>> messageList = new ArrayList<KeyedMessage<String, String>>();
            for (int j = 0; j < 6; j++) { //每个分区6条讯息
                messageList.add(new KeyedMessage<String, String>
                        //format style:  topic,  partition,  message
                        (topic, "partition[" + i + "]", "message[The " + j + " message]"));

                System.out.println("send message to partition: " + i + " , message index is :" + j);
            }
            producer.send(messageList);
        }

    }

    public static void main(String[] args) {
        Thread t = new Thread(new KafkaProducer());
        t.start();
    }
} 