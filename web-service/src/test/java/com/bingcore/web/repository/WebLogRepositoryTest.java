package com.bingcore.web.repository;

import com.bingcore.web.model.other.GoodsPurchaseInfo;
import com.ishansong.common.date.Dates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;


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
public class WebLogRepositoryTest {

    @Autowired
    WebLogDao webLogDao;

    @Test
    public void testSaveLogStat() {
        Long placeTime = 1470150450073l;
        String orderTime = Dates.now("yyyy-MM-dd HH:mm:ss");
        if (placeTime != null) {
            orderTime = Dates.format(placeTime);
        }
        Random rand = new Random();
        int randNum = rand.nextInt(15) + 1;

        webLogDao.savePurchaseInfo(new GoodsPurchaseInfo("BC100" + randNum, 1, "北京", 3, 4545.23, orderTime, true));
    }


}