package com.bingcore.web.model.db;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by xubing on 16/7/21.
 * <p/>
 *
 * @remark:商品信息表
 */
public class Goods implements Serializable {

    private static final long serialVersionUID = 1207793510882216101L;

    //主键id
    private Integer id;

    //物品名称
    private String name;

    //商品价格
    private BigDecimal price;

    //余量
    private Integer count;

    //商品展示图
    private String image;

    //商品url
    private String goodsUrl;

    //商品描述
    private String remark;

    //是否下架
    private int osk;

    //创建时间
    private Long ctime;

    //更新时间
    private Long utime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getOsk() {
        return osk;
    }

    public void setOsk(int osk) {
        this.osk = osk;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getUtime() {
        return utime;
    }

    public void setUtime(Long utime) {
        this.utime = utime;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", image='" + image + '\'' +
                ", goodsUrl='" + goodsUrl + '\'' +
                ", remark='" + remark + '\'' +
                ", osk=" + osk +
                ", ctime=" + ctime +
                ", utime=" + utime +
                '}';
    }
}
