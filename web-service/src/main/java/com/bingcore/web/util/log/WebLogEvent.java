package com.bingcore.web.util.log;

/**
 * <pre>
 * 功能描述：
 *
 * </pre>
 * Author: xubing
 * DateTime: 2016-01-20 下午3:00
 */
public enum WebLogEvent {
    PlACE_ORDER("placeOrder", new PurchaseGoodsHandler());

    private String eventName;
    private BaseLogHander handler;

    private WebLogEvent(String eventName, BaseLogHander handler) {
        this.eventName = eventName;
        this.handler = handler;
    }

    public static WebLogEvent getEventByName(String name) {
        for (WebLogEvent e : WebLogEvent.values()) {
            if (e.eventName.equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    public String getEventName() {
        return eventName;
    }

    public BaseLogHander getHandler() {
        return handler;
    }
}
