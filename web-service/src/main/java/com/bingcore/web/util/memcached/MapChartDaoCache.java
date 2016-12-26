package com.bingcore.web.util.memcached;

import com.bingcore.web.model.db.MapChartData;
import com.bingcore.web.repository.MapChartDao;
import com.google.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xubing on 16/12/18.
 */
@Component
public class MapChartDaoCache {

    @Autowired
    MapChartDao mapChartDao;

    /**
     * 查询地图数据信息
     *
     * @return
     */
    public List<MapChartData> queryAllMapData() {
        List<MapChartData> mapDatas = getMapChartDataCacheValue();
        if (mapDatas == null) {
            mapDatas = mapChartDao.queryMapData();
            MemcachedClient.set(MemcachedKeys.queryAllMapDataKey, mapDatas);
        }

        return mapDatas;
    }

    /**
     * 根据城市城市和商品查询地图数据
     *
     * @param city  城市
     * @param goods 商品名称
     * @return
     */
    public MapChartData queryMapDataByParams(String city, String goods) {
        List<MapChartData> mapDatas = getMapChartDataCacheValue();
        MapChartData mapChartData = null;

        if (mapDatas == null) {
            mapChartData = mapChartDao.queryMapDataByCityAndGoods(city, goods);
        } else {
            for (int i = 0; i < mapDatas.size(); i++) {
                MapChartData mapData = mapDatas.get(i);
                if (Objects.equal(mapData.getCity(), city) && Objects.equal(mapData.getGoods(), goods)) {
                    mapChartData = mapData;
                }
            }
        }

        return mapChartData;
    }

    private List<MapChartData> getMapChartDataCacheValue() {
        try {
            return (List<MapChartData>) MemcachedClient.get(MemcachedKeys.queryAllMapDataKey);
        } catch (NullPointerException e) {
            return null;
        }
    }

}
