package com.bingcore.web.service;

import com.bingcore.web.model.search.WebOrder;
import com.ishansong.common.model.Paging;
import com.ishansong.common.model.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * <pre>
 * 功能描述：测试搜索功能
 *
 * </pre>
 * Author: xubing
 * DateTime: 2016-08-23 下午4:40
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class SearchServiceTest {

    @Autowired
    SearchService searchService;

    @Test
    public void testSearch() {

        Integer pageNo = 1;
        Integer pageSize = 10;
        String st = "2016-08-21";
        String et = "2016-08-22";
        String searchName = "orderNumber";
        String searchValue = "BC17696";

        Response<Paging<WebOrder>> searchResp = searchService.search(pageNo, pageSize, st, et, searchName, searchValue);
        System.out.println("result is: " + searchResp.getData().getData().toString());
    }


}