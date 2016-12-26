package com.bingcore.web.util.system;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xubing on 16/7/26.
 */
public class TestShutdownHook {
    //简单模拟干活的
    static Timer timer = new Timer("job-timer");

    //计数干活次数
    static AtomicInteger count = new AtomicInteger(0);

    /**
     * hook线程
     */
    static class CleanWorkThread extends Thread {
        @Override
        public void run() {
            System.out.println("The JVM Hook is execute, start to clean some work.");
            timer.cancel();
            try {
                Thread.sleep(2 * 1000);//sleep 2s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //将hook线程添加到运行时环境中去
        Runtime.getRuntime().addShutdownHook(new CleanWorkThread());
        System.out.println("main class start ..... ");
        //简单模拟
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                count.getAndIncrement();
                System.out.println("doing job " + count);
                if (count.get() == 10) {  //干了10次退出
                    System.exit(0);
                }
            }
        }, 0, 2 * 1000);

    }
}
