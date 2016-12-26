package com.bingcore.web.util.memcached;

/**
 * Created by xubing on 16/11/2.
 */

import java.util.Date;
import junit.framework.TestCase;


public class Client extends TestCase {

    /**
     * 测试MemcachedUtils类的set方法。
     *
     * @author GaoHuanjie
     */
    public static void testSet1() {
        MemcachedClient.set("set1Description", "调用MemcachedUtils类的set方法，没有设置键值对的存在时长");
        System.out.println(MemcachedClient.get("set1Description").toString());
    }

    /**
     * 测试MemcachedUtils类的set方法。
     *
     * @author GaoHuanjie
     */
    public static void testSet2() {
        MemcachedClient.set("set2Description", "调用MemcachedUtils类的set方法，设置了键值对的存在时长——存在60秒", new Date(1000 * 60));
        System.out.println(MemcachedClient.get("set2Description").toString());
    }

    /**
     * 测试MemcachedUtils类的add方法。
     *
     * @author GaoHuanjie
     */
    public static void testAdd1() {
        MemcachedClient.add("add1Description", "调用MemcachedUtils类的add方法，没有设置键值对的存在时长");
        System.out.println(MemcachedClient.get("add1Description").toString());
    }

    /**
     * 测试MemcachedUtils类的add方法。
     *
     * @author GaoHuanjie
     */
    public static void testAdd2() {
        MemcachedClient.add("add2Description", "调用MemcachedUtils类的add方法，设置了键值对的存在时长——存在60秒", new Date(1000 * 60));
        System.out.println(MemcachedClient.get("add2Description").toString());
    }

    /**
     * 测试MemcachedUtils类的replace方法。
     *
     * @author GaoHuanjie
     */
    public static void testReplace1() {
        MemcachedClient.add("replace1Description", "调用MemcachedUtils类的replace方法，没有设置键值对的存在时长");
        MemcachedClient.replace("replace1Description", "值改变了！！！");
        System.out.println(MemcachedClient.get("replace1Description").toString());
    }

    /**
     * 测试MemcachedUtils类的replace方法。
     *
     * @author GaoHuanjie
     */
    public static void testReplace2() {
        MemcachedClient.add("replace2Description", "调用MemcachedUtils类的replace方法，设置了键值对的存在时长——存在60秒", new Date(1000 * 60));
        MemcachedClient.replace("replace2Description", "值改变了！！！", new Date(1000 * 60));
        System.out.println(MemcachedClient.get("replace2Description").toString());
    }

    /**
     * 测试MemcachedUtils类的get方法。
     *
     * @author GaoHuanjie
     */
    public static void testGet() {
        MemcachedClient.add("getDescription", "调用MemcachedUtils类的get方法，没有设置键值对的存在时长");
        System.out.println(MemcachedClient.get("getDescription").toString());
    }

    /**
     * 测试MemcachedUtils类的delete方法。
     *
     * @author GaoHuanjie
     */
    public static void testDelete1() {
        MemcachedClient.add("delete1Description", "调用MemcachedUtils类的delete方法，没有设置键值对的逾期时长");
        MemcachedClient.delete("delete1Description");
        assertEquals(null, MemcachedClient.get("delete1Description"));
    }

    /**
     * 测试MemcachedUtils类的delete方法。
     *
     * @author GaoHuanjie
     */
    public static void testDelete2() {
        MemcachedClient.set("delete2Description1", "调用MemcachedUtils类的delete方法，设置键值对的逾期时长", new Date(600 * 1000));
        MemcachedClient.delete("delete2Description1", new Date(1000 * 600));
        assertEquals(null, MemcachedClient.get("delete2Description1"));
    }

    /**
     * 测试MemcachedUtils类的flashAll方法。
     *
     * @author GaoHuanjie
     */
    public static void testFlashAll() {
        MemcachedClient.add("flashAllDescription", "调用MemcachedUtils类的delete方法，没有设置键值对的预期时长");
        MemcachedClient.flashAll();
        assertEquals(null, MemcachedClient.get("flashAllDescription"));
    }
}
