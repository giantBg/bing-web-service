package com.bingcore.web.model.search;

import com.ishansong.search.model.Index;
import com.ishansong.search.model.Indexable;

import java.util.Date;

/**
 * 订单信息
 * Author: xubing
 * Date  : 2016-08-18
 * Email : bing.xu@ishansong.com
 */
@Index(name = "orders", type = "order")
public class WebOrder implements Indexable {

    private static final long serialVersionUID = -480026973352206226L;

    //标识
    private String id;

    //订单单号
    private String orderNumber;

    //商品信息
    private WebGoods goods;

    //下单城市
    private String city;

    //购买数量
    private Integer count;

    //消费金额(以分为单位)
    private Integer amt;

    //下单时间
    private Date placeTime;

    //下单结果
    private String result;

    @Override
    public Object getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public WebGoods getGoods() {
        return goods;
    }

    public void setGoods(WebGoods goods) {
        this.goods = goods;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getAmt() {
        return amt;
    }

    public void setAmt(Integer amt) {
        this.amt = amt;
    }

    public Date getPlaceTime() {
        return placeTime;
    }

    public void setPlaceTime(Date placeTime) {
        this.placeTime = placeTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "WebOrder{" +
                "id='" + id + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", goods=" + goods +
                ", city='" + city + '\'' +
                ", count=" + count +
                ", amt=" + amt +
                ", placeTime=" + placeTime +
                ", result='" + result + '\'' +
                '}';
    }
}
