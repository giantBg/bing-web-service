package com.bingcore.web.model.chart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xubing on 2016/04/16.
 */
public class SeriesOfMap implements Serializable {

    private static final long serialVersionUID = 1207793510882216103L;

    private String name;
    private String type;
    private String mapType;
    private Boolean roam;
    private String itemStyle;
    private List<MapData> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public Boolean getRoam() {
        return roam;
    }

    public void setRoam(Boolean roam) {
        this.roam = roam;
    }

    public String getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(String itemStyle) {
        this.itemStyle = itemStyle;
    }

    public List<MapData> getData() {
        return data;
    }

    public void setData(List<MapData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SeriesOfMap{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", mapType='" + mapType + '\'' +
                ", roam=" + roam +
                ", itemStyle=" + itemStyle +
                ", data=" + data +
                '}';
    }
}

