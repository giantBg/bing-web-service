package com.bingcore.web.model.other;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by xubing on 16/8/01.
 *
 * @Author xubing
 * @DateTime 2016-08-01 22:14
 */
@Document(collection = "goods_purchase")
public class GoodsPurchaseInfo implements Serializable {

    private static final long serialVersionUID = -1375544675003626653L;

    //订单单号
    @Id
    private String orderNumber;

    //商品id
    private Integer goodsId;

    //城市
    private String city;

    //购买数量
    private Integer count;

    //消费金额
    private Double monetary;

    //下单时间
    private String placeTime;

    //下单结果
    private Boolean success;


    @PersistenceConstructor
    public GoodsPurchaseInfo(String orderNumber, Integer goodsId, String city, Integer count, Double monetary, String placeTime, Boolean success) {
        this.orderNumber = orderNumber;
        this.goodsId = goodsId;
        this.city = city;
        this.count = count;
        this.monetary = monetary;
        this.placeTime = placeTime;
        this.success = success;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
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

    public Double getMonetary() {
        return monetary;
    }

    public void setMonetary(Double monetary) {
        this.monetary = monetary;
    }

    public String getPlaceTime() {
        return placeTime;
    }

    public void setPlaceTime(String placeTime) {
        this.placeTime = placeTime;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "GoodsPurchaseInfo{" +
                "orderNumber='" + orderNumber + '\'' +
                ", goodsId=" + goodsId +
                ", city='" + city + '\'' +
                ", count=" + count +
                ", monetary=" + monetary +
                ", placeTime='" + placeTime + '\'' +
                ", success=" + success +
                '}';
    }
}
