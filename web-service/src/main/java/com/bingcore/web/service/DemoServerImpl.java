package com.bingcore.web.service;

import com.google.common.base.Joiner;
import com.ishansong.common.date.Dates;
import org.springframework.stereotype.Service;

/**
 * Created by xubing on 16/5/21.
 */
@Service("demoServer")
public class DemoServerImpl implements DemoServer {

    @Override
    public String sayHello(String str) {

        return Joiner.on(" ").join("(Welcome ", str, "at", Dates.now("yyyy-MM-dd HH:mm:ss"), ")");
    }
}
