package com.bingcore.web.util.kafka.case1;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by xubing on 16/8/3.
 */
public class kafkaProducer extends Thread {

    private String topic;

    public kafkaProducer(String topic) {
        super();
        this.topic = topic;
    }


    @Override
    public void run() {
        Producer producer = createProducer();
        int i = 0;
        while (true) {
            producer.send(new KeyedMessage<Integer, String>(topic, "message: " + i++));

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Producer createProducer() {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183");//声明zk
        properties.put("serializer.class", StringEncoder.class.getName());//配置value的序列化类
        properties.put("metadata.broker.list", "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094");// 声明kafka broker
        properties.put("partitioner.class", "com.bingcore.web.util.kafka.case1.MyPartitioner");
        //http://blog.csdn.net/honglei915/article/details/37697655

        return new Producer<Integer, String>(new ProducerConfig(properties));
    }


    public static void main(String[] args) {
        new kafkaProducer("bing_cluster_log").start();// 使用kafka集群中创建好的主题 test

    }

}
