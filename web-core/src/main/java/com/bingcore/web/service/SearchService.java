package com.bingcore.web.service;

import com.bingcore.web.model.search.WebOrder;
import com.ishansong.common.model.Paging;
import com.ishansong.common.model.Response;

/**
 * Created by xubing on 16/8/21.
 *
 * @Author xubing
 * @DateTime 2016-08-21 16:14
 * @Function order搜索接口
 */
public interface SearchService {

    /**
     * 索引订单
     *
     * @param orderNumber 订单号
     * @return 索引成功返回true，反之false
     */
    Response<Boolean> index(String orderNumber);


    /**
     * 查询单个订单
     *
     * @param orderNumber 订单号
     */
    Response<WebOrder> queryByOrderNumber(String orderNumber);


    /**
     * 分页查询订单
     *
     * @param pageNo   页号
     * @param pageSize 分页大小
     * @param st       起始时间
     * @param et       结束时间
     * @param srName   搜索字段
     * @param srValue  搜索值
     * @return 订单分页对象
     */
    Response<Paging<WebOrder>> search(
            Integer pageNo, Integer pageSize, String st, String et, String srName, String srValue);


    /**
     * 统计订单
     *
     * @param st      起始时间
     * @param et      结束时间
     * @param srName  搜索字段
     * @param srValue 搜索值
     * @return 订单数目
     */
    Response<Long> count(String st, String et, String srName, String srValue);

}
