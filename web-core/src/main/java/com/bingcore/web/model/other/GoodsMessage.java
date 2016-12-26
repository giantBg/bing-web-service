package com.bingcore.web.model.other;

import com.bingcore.web.model.db.Goods;

import java.io.Serializable;

/**
 * Created by xubing on 16/7/23.
 *
 * @Author xubing
 * @DateTime 2016-07-23 16:14
 */
public class GoodsMessage implements Serializable {

    private static final long serialVersionUID = -1375544675003626652L;

    public GoodsMessage() {
    }

    public GoodsMessage(String city, Goods goods, Integer count) {
        this.city = city;
        this.goods = goods;
        this.count = count;
    }

    //城市
    private String city;

    //商品信息
    private Goods goods;

    //购买数量
    private Integer count;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "GoodsMessage{" +
                "city='" + city + '\'' +
                ", goods=" + goods +
                ", count=" + count +
                '}';
    }
}
