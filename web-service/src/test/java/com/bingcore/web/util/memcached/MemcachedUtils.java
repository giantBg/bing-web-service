package com.bingcore.web.util.memcached;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

/**
 * Memcached工具类
 *
 * @author GaoHuanjie
 */
public class MemcachedUtils {

    private static MemCachedClient memCachedClient;

    static {
        /************************************配置Memcached**************************************/
        SockIOPool sockIOPool = SockIOPool.getInstance();

        sockIOPool.setServers(new String[]{"127.0.0.1:11211"});//设置memcached服务器地址
        sockIOPool.setWeights(new Integer[]{3});                //设置每个MemCached服务器权重
        sockIOPool.setFailover(true);                        //当一个memcached服务器失效的时候是否去连接另一个memcached服务器.
        sockIOPool.setInitConn(10);                            //初始化时对每个服务器建立的连接数目
        sockIOPool.setMinConn(10);                            //每个服务器建立最小的连接数
        sockIOPool.setMaxConn(100);                            //每个服务器建立最大的连接数
        sockIOPool.setMaintSleep(30);                        //自查线程周期进行工作，其每次休眠时间
        sockIOPool.setNagle(false);                            //Socket的参数，如果是true在写数据时不缓冲，立即发送出去。Tcp的规则是在发送一个包之前，包的发送方会等待远程接收方确认已收到上一次发送过来的包；这个方法就可以关闭套接字的缓存——包准备立即发出。
        sockIOPool.setSocketTO(3000);                        //Socket阻塞读取数据的超时时间
        sockIOPool.setAliveCheck(true);                        //设置是否检查memcached服务器是否失效
        sockIOPool.setMaxIdle(1000 * 30 * 30);                    // 设置最大处理时间
        sockIOPool.setSocketConnectTO(0);                    //连接建立时对超时的控制

        sockIOPool.initialize();                                // 初始化连接池
        if (memCachedClient == null) {
            memCachedClient = new MemCachedClient();
            memCachedClient.setPrimitiveAsString(true);        //是否将基本类型转换为String方法
        }
    }

    private MemcachedUtils() {
    }

    /**
     * 向缓存添加键值对。注意：如果键已经存在，则之前的键对应的值将被替换。
     *
     * @author GaoHuanjie
     */
    public static boolean set(String key, Object value) {
        try {
            return memCachedClient.set(key, value);
        } catch (Exception e) {
            MemcachedLogUtils.writeLog("Memcached set方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
            return false;
        }
    }

    /**
     * 向缓存添加键值对并为该键值对设定逾期时间（即多长时间后该键值对从Memcached内存缓存中删除，比如： new Date(1000*10)，则表示十秒之后从Memcached内存缓存中删除）。注意：如果键已经存在，则之前的键对应的值将被替换。
     *
     * @author GaoHuanjie
     */
    public static boolean set(String key, Object value, Date expire) {
        try {
            return memCachedClient.set(key, value, expire);
        } catch (Exception e) {
            MemcachedLogUtils.writeLog("Memcached set方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
            return false;
        }
    }

    /**
     * 向缓存添加键值对。注意：仅当缓存中不存在键时，才会添加成功。
     *
     * @author GaoHuanjie
     */
    public static boolean add(String key, Object value) {
        try {
            if (get(key) != null) {
                MemcachedLogUtils.writeLog("Memcached add方法报错，key值：" + key + "\r\n" + exceptionWrite(new Exception("Memcached内存缓存中已经存在该键值对")));
                return false;
            } else {
                return memCachedClient.add(key, value);
            }
        } catch (Exception e) {
            MemcachedLogUtils.writeLog("Memcached add方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
            return false;
        }
    }

    /**
     * 向缓存添加键值对并为该键值对设定逾期时间（即多长时间后该键值对从Memcached内存缓存中删除，比如： new Date(1000*10)，则表示十秒之后从Memcached内存缓存中删除）。注意：仅当缓存中不存在键时，才会添加成功。
     *
     * @author GaoHuanjie
     */
    public static boolean add(String key, Object value, Date expire) {
        try {
            if (get(key) != null) {
                MemcachedLogUtils.writeLog("Memcached add方法报错，key值：" + key + "\r\n" + exceptionWrite(new Exception("Memcached内存缓存中已经存在该键值对")));
                return false;
            } else {
                return memCachedClient.add(key, value, expire);
            }
        } catch (Exception e) {
            MemcachedLogUtils.writeLog("Memcached add方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
            return false;
        }
    }

    /**
     * 根据键来替换Memcached内存缓存中已有的对应的值。注意：只有该键存在时，才会替换键相应的值。
     *
     * @author GaoHuanjie
     */
    public static boolean replace(String key, Object newValue) {
        try {
            return memCachedClient.replace(key, newValue);
        } catch (Exception e) {
            MemcachedLogUtils.writeLog("Memcached replace方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
            return false;
        }
    }

    /**
     * 根据键来替换Memcached内存缓存中已有的对应的值并设置逾期时间（即多长时间后该键值对从Memcached内存缓存中删除，比如： new Date(1000*10)，则表示十秒之后从Memcached内存缓存中删除）。注意：只有该键存在时，才会替换键相应的值。
     *
     * @author GaoHuanjie
     */
    public static boolean replace(String key, Object newValue, Date expireDate) {
        try {
            return memCachedClient.replace(key, newValue, expireDate);
        } catch (Exception e) {
            MemcachedLogUtils.writeLog("Memcached replace方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
            return false;
        }
    }

    /**
     * 根据键获取Memcached内存缓存管理系统中相应的值
     *
     * @author GaoHuanjie
     */
    public static Object get(String key) {
        try {
            return memCachedClient.get(key);
        } catch (Exception e) {
            MemcachedLogUtils.writeLog("Memcached get方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
            return null;
        }
    }

    /**
     * 根据键删除memcached中的键/值对
     *
     * @author GaoHuanjie
     */
    public static boolean delete(String key) {
        try {
            return memCachedClient.delete(key);
        } catch (Exception e) {
            MemcachedLogUtils.writeLog("Memcached delete方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
            return false;
        }
    }

    /**
     * 根据键和逾期时间（例如：new Date(1000*10)：十秒后过期）删除 memcached中的键/值对
     *
     * @author GaoHuanjie
     */
    public static boolean delete(String key, Date expireDate) {
        try {
            return memCachedClient.delete(key, expireDate);
        } catch (Exception e) {
            MemcachedLogUtils.writeLog("Memcached delete方法报错，key值：" + key + "\r\n" + exceptionWrite(e));
            return false;
        }
    }

    /**
     * 清理缓存中的所有键/值对
     *
     * @author GaoHuanjie
     */
    public static boolean flashAll() {
        try {
            return memCachedClient.flushAll();
        } catch (Exception e) {
            MemcachedLogUtils.writeLog("Memcached flashAll方法报错\r\n" + exceptionWrite(e));
            return false;
        }
    }

    /**
     * 返回String类型的异常栈信息
     *
     * @author GaoHuanjie
     */
    private static String exceptionWrite(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        printWriter.flush();
        return stringWriter.toString();
    }

    /**
     * Memcached日志记录工具
     *
     * @author GaoHuanjie
     */
    private static class MemcachedLogUtils {

        private static FileWriter fileWriter;
        private static BufferedWriter logWrite;
        private final static String PID = ManagementFactory.getRuntimeMXBean().getName();// 通过找到对应的JVM进程获取PID

        /**
         * 初始化Memcached日志写入流
         *
         * @author GaoHuanjie
         */
        static {
            try {
                String osName = System.getProperty("os.name");
                if (osName.contains("Windows")) {
                    fileWriter = new FileWriter("D:\\memcached.log", true);
                } else {
                    fileWriter = new FileWriter("/usr/local/logs/memcached.log", true);
                }
                logWrite = new BufferedWriter(fileWriter);
            } catch (IOException iOException) {
                iOException.printStackTrace();
                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                    if (logWrite != null) {
                        logWrite.close();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

        /**
         * 写入日志信息
         *
         * @author GaoHuanjie
         */
        public static void writeLog(String logContent) {
            try {
                logWrite.write("[" + PID + "] " + "- [" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]\r\n" + logContent);
                logWrite.newLine();
                logWrite.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
