package com.bingcore.web.util.log;

import com.ishansong.common.model.Response;
import com.ishansong.log.Loggers;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: xubing
 * Date:   16/08/02 下午17:44
 */

public abstract class BaseLogHander {

    private static final int minCoreSize = Runtime.getRuntime().availableProcessors() / 2 + 1;
    private static final int maxCoreSize = Runtime.getRuntime().availableProcessors() * 2;
    private static final int aliveTime = 60;
    private static final ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue<Runnable>(10240);

    private ThreadPoolExecutor pool = new ThreadPoolExecutor(minCoreSize, maxCoreSize, aliveTime, TimeUnit.SECONDS, arrayBlockingQueue);

    protected Logger logger = Loggers.get("consumer");

    /**
     * 异步任务执行
     *
     * @param params 必要参数
     * @return 执行结果 成功true 反之false
     */
    public abstract Response<Object> exec(Map<String, String> params);


    /**
     * 执行异步事件
     *
     * @param task
     */
    protected void eventDeal(Runnable task) {
        pool.execute(task);
    }

}
