package com.bingcore.web.service;

import com.bingcore.web.enums.MapType;
import com.bingcore.web.model.chart.ChartOfMap;
import com.bingcore.web.model.chart.MapData;
import com.bingcore.web.model.chart.SeriesOfMap;
import com.bingcore.web.model.db.MapChartData;
import com.bingcore.web.util.memcached.MapChartDaoCache;
import com.google.common.base.Throwables;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.ishansong.common.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xubing on 16/5/11.
 *
 * @Author xubing
 * @DateTime 2016-05-11 16:14
 * @Function map图表服务实现类
 */
@Service("mapChartService")
public class MapChartServiceImpl implements MapChartService {

    private final static Logger logger = LoggerFactory.getLogger("web");


    @Autowired
    MapChartDaoCache mapChartDaoCache;

    /**
     * 地图数据结果
     *
     * @return 图表信息
     */
    @Override
    public Response<ChartOfMap> getMapData() {

        try {

            List<MapChartData> mapLists = mapChartDaoCache.queryAllMapData();

            List<String> legendList = Lists.newArrayList();
            List<SeriesOfMap> seriesList = Lists.newArrayList();
            ChartOfMap chart = new ChartOfMap();
            chart.setTitle("集团产品销量分布");
            chart.setSubTitle("纯属虚构");
            chart.setLink("#");
            chart.setLegendList(legendList);
            chart.setSeriesList(seriesList);

            Multimap<MapType, MapData> dataMap = ArrayListMultimap.create();
            for (MapType at : MapType.values()) {
                legendList.add(at.getName());
                SeriesOfMap series = new SeriesOfMap();
                series.setName(at.getName());
                series.setType("map");
                series.setMapType("china");
                series.setRoam(false);
                series.setItemStyle(null);
                series.setData((List) dataMap.get(at));
                seriesList.add(series);
            }

            for (MapChartData stat : mapLists) {
                switch (MapType.getByName(stat.getGoods())) {
                    case GOODS_ONE:
                        dataMap.put(MapType.GOODS_ONE, MapData.newData(stat.getCity(), stat.getCount()));
                        break;
                    case GOODS_TWO:
                        dataMap.put(MapType.GOODS_TWO, MapData.newData(stat.getCity(), stat.getCount()));
                        break;
                    case GOODS_THREE:
                        dataMap.put(MapType.GOODS_THREE, MapData.newData(stat.getCity(), stat.getCount()));
                        break;
                }
            }

            return Response.ok(chart);
        } catch (Exception e) {
            logger.error("failed to get map data, cause: {}", Throwables.getStackTraceAsString(e));
            return Response.notOk(e.getMessage());
        }
    }

}
