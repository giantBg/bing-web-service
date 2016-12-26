package com.bingcore.web.util;

import com.ishansong.common.config.Config;
import com.ishansong.common.config.ConfigCenter;

/**
 * <pre>
 * 功能描述：
 * 错误消息模板
 * </pre>
 * Author: xubing
 * DateTime: 2015-05-16 16:21
 */
public class Messages {

    private static Config config = null;

    static {
        try {
            //读取配置文件配置
            config = ConfigCenter.getConfig("messages.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getString(String code) {
        return config.getString(code);
    }

}
