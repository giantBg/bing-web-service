package com.bingcore.web.util.nsq;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.ishansong.common.json.Jsoner;
import com.ishansong.log.Loggers;
import com.ishansong.nsq.NSQLookup;
import com.ishansong.nsq.NSQMessage;
import com.ishansong.nsq.NSQMessageCallback;
import com.ishansong.nsq.factory.NSQConsumerFactory;
import com.ishansong.nsq.lookup.NSQLookupDynMapImpl;
import org.slf4j.Logger;

import java.lang.reflect.ParameterizedType;

/**
 * Created by xubing on 16/5/11.
 *
 * @Author xubing
 * @DateTime 2016-07-14 16:14
 * @Function nsq消费者类
 */
@SuppressWarnings("unchecked")
public abstract class MsgConsumer<T> {

    protected static final Logger logger = Loggers.get("web");

    protected final Class generic =
            (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0];

    protected MsgConsumer(MsgConfig config, String topic) {
        NSQLookup lookup = new NSQLookupDynMapImpl();
        lookup.addAddr(config.getHost(), config.getPort());

        logger.info("start to init nsq consumer, host={},port={},topic={}", config.getHost(), config.getPort(), topic);
        NSQConsumerFactory.newConsumer(lookup, topic, "test_channel", 300, new NSQMessageCallback() {
            @Override
            public void message(NSQMessage message) {
                try {
                    message.finished();
                    String msg = new String(message.getMessage(), "UTF-8");
                    if (Strings.isNullOrEmpty(msg)) {
                        logger.info("consume msg is empty.");
                        return;
                    }
                    T m = (T) Jsoner.DEFAULT.fromJson(msg, generic);
                    if (m != null) {
                        onMessage(m);
                    }
                } catch (Exception e) {
                    message.requeue();
                    logger.error("failed to consume message({}), cause: {}",
                            message, Throwables.getStackTraceAsString(e));
                }
            }

            @Override
            public void error(Exception e) {
                onError(e);
            }
        });
    }

    protected abstract void onMessage(T t);

    void onError(Exception e) {
        logger.error("occur exception when consume message: {}",
                Throwables.getStackTraceAsString(e));
    }
}
