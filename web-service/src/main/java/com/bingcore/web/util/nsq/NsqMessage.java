package com.bingcore.web.util.nsq;

import java.io.Serializable;

/**
 * Created by xubing on 16/5/11.
 *
 * @Author xubing
 * @DateTime 2016-05-11 16:14
 */
public class NsqMessage implements Serializable {

    private static final long serialVersionUID = -1375544675003626651L;

    /**
     * test
     */
    private String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @Override
    public String toString() {
        return "NsqMessage{" +
                "Message='" + Message + '\'' +
                '}';
    }
}
