package com.bingcore.web.service;

import com.bingcore.web.model.db.Goods;
import com.bingcore.web.model.other.GoodsPurchaseInfo;
import com.ishansong.common.model.Paging;
import com.ishansong.common.model.Response;

import java.util.Date;
import java.util.List;


/**
 * Created by xubing on 16/7/21.
 *
 * @Author xubing
 * @DateTime 2016-07-21 21:14
 * @Function 商品信息服务接口类
 */
public interface GoodsService {


    /**
     * 查询所有商品信息
     *
     * @return
     */
    public Response<List<Goods>> queryAllGoodsInfo();


    /**
     * 查询某个商品信息
     *
     * @return
     */
    public Response<Goods> queryOneGoodsById(Integer id);


    /**
     * 确认下单接口
     *
     * @param goods 商品信息
     * @param city  城市信息
     * @param cnt   购买数量
     * @return
     */
    public Response<Boolean> confirmPlaceOrder(Goods goods, String city, Integer cnt);


    /**
     * 分页查询购买记录
     *
     * @param pageNo    页号
     * @param pageSize  分页大小
     * @param startDate 起始时间
     * @param endDate   终止时间
     * @param srName    查询项
     * @param srValue   查询数值
     * @return goodsInfo
     */
    public Response<Paging<GoodsPurchaseInfo>> paging(Integer pageNo, Integer pageSize, Date startDate, Date endDate, String srName, String srValue);

}
