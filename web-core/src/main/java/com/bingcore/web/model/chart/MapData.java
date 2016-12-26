package com.bingcore.web.model.chart;

import java.io.Serializable;

/**
 * Created by xubing on 2016/5/11.
 */
public class MapData implements Serializable {

    private static final long serialVersionUID = 1207793510882216102L;

    private String name;
    private int value;

    public MapData() {
    }

    /**
     * 构造器
     *
     * @param name  key
     * @param value value
     */
    public MapData(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static MapData newData(String name, Integer value) {
        return new MapData(name, value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MapData{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
