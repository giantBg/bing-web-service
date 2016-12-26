package com.bingcore.web.model.db;

import java.io.Serializable;

/**
 * Created by xubing on 16/5/11.
 *
 * @remark: 地图信息表
 */
public class MapChartData implements Serializable {

    private static final long serialVersionUID = 1207793510882216100L;

    //主键id
    private Integer id;

    //物品名称
    private String goods;

    //城市
    private String city;

    //销量
    private Integer count;

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

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
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
        return "MapChartData{" +
                "id=" + id +
                ", goods='" + goods + '\'' +
                ", city='" + city + '\'' +
                ", count=" + count +
                ", ctime=" + ctime +
                ", utime=" + utime +
                '}';
    }
}
