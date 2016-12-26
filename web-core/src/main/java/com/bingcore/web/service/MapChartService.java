package com.bingcore.web.service;

import com.bingcore.web.model.chart.ChartOfMap;
import com.ishansong.common.model.Response;

/**
 * Created by xubing on 16/5/11.
 *
 * @Author xubing
 * @DateTime 2016-05-11 16:14
 * @Function map图表服务接口类
 */
public interface MapChartService {


    /**
     * 地图数据结果
     *
     * @return 图表信息
     */
    public Response<ChartOfMap> getMapData();

}
