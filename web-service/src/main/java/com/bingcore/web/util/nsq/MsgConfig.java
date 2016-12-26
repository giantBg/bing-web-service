package com.bingcore.web.util.nsq;

/**
 * Created by xubing on 16/5/11.
 *
 * @Author xubing
 * @DateTime 2016-05-11 16:14
 */
public class MsgConfig {

    private String host;

    private Integer port;

    public MsgConfig(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }
}
