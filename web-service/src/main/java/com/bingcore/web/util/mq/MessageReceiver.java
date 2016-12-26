package com.bingcore.web.util.mq;

import com.ishansong.activemq.DefaultMessageReceiver;
import com.ishansong.activemq.message.DefaultMessage;
import com.ishansong.log.Loggers;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Created by xubing on 16/09/06.
 *
 * @Author xubing
 * @DateTime 2016-09-06 22:14
 */
@Service
public abstract class MessageReceiver<T> extends DefaultMessageReceiver {

    protected static final Logger logger = Loggers.get("web");

    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        logger.info("activeMq is received... Message=" + message);
        if (message instanceof ActiveMQObjectMessage) {

            ActiveMQObjectMessage activeMQObjectMessage = (ActiveMQObjectMessage) message;

            Object object = activeMQObjectMessage.getObject();
            if (object == null) {
                logger.error("receiver null object with active mq message");
                return;
            }

            if (object instanceof DefaultMessage) {
                T obj = (T) object;
                exec(obj);
            }
        }
    }

    /**
     * 具体的业务处理方法
     *
     * @param msg 从mq中取出的消息
     */
    protected abstract void exec(T msg);

}
