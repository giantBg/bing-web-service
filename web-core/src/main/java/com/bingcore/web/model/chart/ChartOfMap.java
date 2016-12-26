package com.bingcore.web.model.chart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xubing on 16/5/11.
 */
public class ChartOfMap implements Serializable {

    private static final long serialVersionUID = 1207793510882216101L;

    /**
     * 标题
     */
    private String title;

    /**
     * 子标题
     */
    private String subTitle;

    /**
     * 图表链接
     */
    private String link;

    /**
     * 图例
     */
    private List<String> legendList;

    /**
     * 数据
     */
    private List<SeriesOfMap> seriesList;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getLegendList() {
        return legendList;
    }

    public void setLegendList(List<String> legendList) {
        this.legendList = legendList;
    }

    public List<SeriesOfMap> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(List<SeriesOfMap> seriesList) {
        this.seriesList = seriesList;
    }

    @Override
    public String toString() {
        return "ChartOfMap{" +
                "title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", link='" + link + '\'' +
                ", legendList=" + legendList +
                ", seriesList=" + seriesList +
                '}';
    }
}
