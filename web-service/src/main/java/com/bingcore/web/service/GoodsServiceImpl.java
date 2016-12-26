package com.bingcore.web.service;

import com.bingcore.web.model.db.Goods;
import com.bingcore.web.model.other.GoodsPurchaseInfo;
import com.bingcore.web.repository.GoodsDao;
import com.bingcore.web.repository.WebLogDao;
import com.google.common.base.Throwables;
import com.ishansong.common.date.Dates;
import com.ishansong.common.model.Page;
import com.ishansong.common.model.Paging;
import com.ishansong.common.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by xubing on 16/7/21.
 *
 * @Author xubing
 * @DateTime 2016-07-21 21:14
 * @Function 商品信息服务实现类
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    private final static Logger logger = LoggerFactory.getLogger("web");

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    WebLogDao webLogDao;

    /**
     * 查询所有商品信息
     *
     * @return
     */
    @Override
    public Response<List<Goods>> queryAllGoodsInfo() {
        try {
            return Response.ok(goodsDao.queryAllGoodsInfo());
        } catch (Exception e) {
            logger.error("failed to query  all goods, cause: {}", Throwables.getStackTraceAsString(e));
            return Response.notOk(e.getMessage());
        }
    }

    /**
     * 查询某个商品信息
     *
     * @return
     */
    @Override
    public Response<Goods> queryOneGoodsById(Integer id) {
        try {
            return Response.ok(goodsDao.queryGoodsById(id));
        } catch (Exception e) {
            logger.error("failed to query  goods {}, cause: {}", id, Throwables.getStackTraceAsString(e));
            return Response.notOk(e.getMessage());
        }
    }


    /**
     * 下单确认
     *
     * @param goods 商品信息
     * @param city  城市信息
     * @param cnt   购买数量
     * @return
     */
    @Override
    public Response<Boolean> confirmPlaceOrder(Goods goods, String city, Integer cnt) {
        try {
            logger.info("start to place order, goods:{}, city:{}", goods, city);

            return Response.ok(goodsDao.confirmToPlaceOrder(goods, city, cnt));
        } catch (Exception e) {
            logger.error("failed to place order  goods {}, cause: {}", goods, Throwables.getStackTraceAsString(e));
            return Response.notOk(e.getMessage());
        }
    }

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
    public Response<Paging<GoodsPurchaseInfo>> paging(Integer pageNo, Integer pageSize, Date startDate, Date endDate, String srName, String srValue) {
        Response<Paging<GoodsPurchaseInfo>> resp = new Response<Paging<GoodsPurchaseInfo>>();
        try {
            Page page = new Page(pageNo, pageSize);
            String startTime = Dates.format(startDate);
            String endTime = Dates.format(endDate);
            Paging<GoodsPurchaseInfo> goodsInfos = webLogDao.pageQueryGoodsInfo(startTime, endTime, srName, srValue, page.getLimit(), page.getOffset());
            resp.setData(goodsInfos);
        } catch (Exception e) {
            logger.error("failed to find goods  records(pageNo={}, pageSize{}), cause: {}",
                    pageNo, pageSize, Throwables.getStackTraceAsString(e));
        }
        return resp;
    }

}
