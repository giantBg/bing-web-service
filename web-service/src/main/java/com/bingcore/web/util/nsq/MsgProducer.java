package com.bingcore.web.util.nsq;

import com.google.common.base.Throwables;
import com.ishansong.common.json.Jsoner;
import com.ishansong.log.Loggers;
import com.ishansong.nsq.NSQProducer;
import com.ishansong.nsq.factory.NSQProducerFactory;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 25/11/15
 */
public class MsgProducer {

    private static Logger logger = Loggers.get("producer");

    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("web-notify-producer");
            return t;
        }
    });

    private NSQProducer producer;

    public MsgProducer(MsgConfig config){
        producer = NSQProducerFactory.newProducerForLookup(config.getHost(), config.getPort());
        producer.start();
        logger.info("OrderNotifyProducer started");
    }

    public void produce(final String topic, final Object msg){
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    producer.produce(topic, Jsoner.DEFAULT.toJson(msg).getBytes("UTF-8"));
                }  catch (Exception e) {
                    logger.error("failed to produce msg(topic={}, msg={}), cause: {}",
                            topic, msg, Throwables.getStackTraceAsString(e));
                }
            }
        });
    }
}
