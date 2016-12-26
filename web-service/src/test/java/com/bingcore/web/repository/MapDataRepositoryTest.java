package com.bingcore.web.repository;

import com.bingcore.web.enums.MapType;
import com.bingcore.web.model.db.MapChartData;
import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * <pre>
 * 功能描述：测试订单统计dao
 *
 * </pre>
 * Author: xubing
 * DateTime: 2016-03-04 下午4:40
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class MapDataRepositoryTest {

    @Autowired
    MapChartDao mapChartDao;

    @Test
    public void testSaveOrderStat() {

        String[] strCity1 = new String[]{"北京", "上海", "重庆", "河北", "河南", "云南", "辽宁", "黑龙江", "湖南", "安徽", "山东", "新疆", "江苏", "浙江", "江西", "湖北", "广西", "甘肃", "山西", "内蒙古"
                , "陕西", "吉林", "福建", "贵州", "广东", "青海", "西藏", "四川", "宁夏", "海南", "台湾", "香港", "澳门"};

        String[] strCity2 = new String[]{"北京", "上海", "重庆", "广西", "甘肃", "山西", "内蒙古"
                , "陕西", "吉林", "福建", "贵州", "广东", "青海", "西藏", "四川", "宁夏", "海南", "台湾", "香港", "澳门"};

        String[] strCity3 = new String[]{"北京", "上海", "重庆", "广西", "甘肃", "山西", "内蒙古", "陕西", "吉林"};


        for (MapType at : MapType.values()) {
            MapChartData mapChartData = new MapChartData();
            //save data by type
            switch (at) {
                case GOODS_ONE: {
                    buildSaveData(mapChartData, at.getName(), strCity1);
                }
                break;
                case GOODS_TWO: {
                    buildSaveData(mapChartData, at.getName(), strCity2);
                }
                break;
                case GOODS_THREE: {
                    buildSaveData(mapChartData, at.getName(), strCity3);
                }
                break;
            }
        }

    }

    private void buildSaveData(MapChartData mapData, String goodName, String[] cities) {
        List<String> cityList = Lists.newArrayList(cities);
        for (String city : cityList) {
            mapData.setGoods(goodName);
            mapData.setCity(city);
            mapData.setCount(Long.valueOf(Math.round(Math.random() * 1000)).intValue());

            //save data
            Boolean saveResult = mapChartDao.saveMapData(mapData);
            Assert.assertTrue("save unSuccessfully!", saveResult);
        }
    }

}