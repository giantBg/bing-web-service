package com.bingcore.web;

import com.bingcore.web.util.ShutdownHook;
import com.bingcore.web.util.log.kafkaConsumer;
import com.ishansong.log.Loggers;
import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务启动器
 */
public class Server {

    private static ClassPathXmlApplicationContext context;
    private static final Logger log = Loggers.get(Server.class);

    /**
     * 启动服务
     *
     * @throws InterruptedException
     */
    public void run() throws InterruptedException {

        //program exits hooks, calmly to shut down
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook()));

        //load spring service
        context = new ClassPathXmlApplicationContext(new String[]{"classpath:application-service.xml", "classpath:application-context.xml"});
        context.start();

        //start logConsumer server
//        WebLogConsumer.start();
        new kafkaConsumer("dev_stat_log").start();

        log.info("web Server Start Ok!");
        while (true) {
            Thread.sleep(Long.MAX_VALUE);
        }
    }

    public static void main(String[] args) throws Exception {

        Server server = new Server();
        try {
            server.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("Server Start Successful");
    }

}
