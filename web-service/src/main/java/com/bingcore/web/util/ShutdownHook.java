package com.bingcore.web.util;

import com.bingcore.web.util.log.WebLogConsumer;
import com.ishansong.log.Loggers;
import org.slf4j.Logger;

/**
 * Created by xubing on 16/07/26.
 *
 * @Author xubing
 * @DateTime 2016-07-26
 * @Function web服务器服务关闭钩子
 */
public class ShutdownHook implements Runnable {
    private Logger logger = Loggers.get(ShutdownHook.class);

    public ShutdownHook() {
        logger.info("------------execute  bingcore-web-service shutdown hook-------------");
    }

    @Override
    public void run() {
        //do business things

        //step1: stop consumer logs
        WebLogConsumer.shutdown = true;

        logger.info("-------------system ready to exit---------");
    }
}
