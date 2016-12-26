package com.bingcore.web.model.search;

import java.io.Serializable;

/**
 * Created by xubing on 16/7/21.
 * <p/>
 *
 * @remark:商品信息表
 */
public class WebGoods implements Serializable {

    private static final long serialVersionUID = 1207793510882216107L;

    //主键id
    private Integer id;

    //物品名称
    private String name;

    //商品单价(以分为单位)
    private Integer price;

    //余量
    private Integer count;

    //商品描述
    private String remark;

    //是否下架
    private Boolean flag;

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "WebGoods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", remark='" + remark + '\'' +
                ", flag=" + flag +
                '}';
    }
}
