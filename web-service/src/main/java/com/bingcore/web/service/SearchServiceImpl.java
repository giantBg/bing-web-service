package com.bingcore.web.service;

import com.bingcore.web.model.db.Goods;
import com.bingcore.web.model.other.GoodsPurchaseInfo;
import com.bingcore.web.model.search.WebGoods;
import com.bingcore.web.model.search.WebOrder;
import com.bingcore.web.repository.WebLogDao;
import com.bingcore.web.util.Messages;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.math.IntMath;
import com.ishansong.common.date.Dates;
import com.ishansong.common.model.Page;
import com.ishansong.common.model.Paging;
import com.ishansong.common.model.Response;
import com.ishansong.search.core.QueryBuilder;
import com.ishansong.search.core.Searcher;
import com.ishansong.search.model.OrderType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by xubing on 16/8/21.
 *
 * @Author xubing
 * @DateTime 2016-08-21 11:54
 * @Function 搜索服务实现类
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService {

    private final static Logger logger = LoggerFactory.getLogger("search");

    private static final int coreSize = Runtime.getRuntime().availableProcessors() + 1;
    private static final int maxSize = coreSize * 2;
    private static final int keepAliveTime = 60;
    private static final int queueSize = 10240;
    private static ExecutorService executor = new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, TimeUnit.DAYS,
            new ArrayBlockingQueue<Runnable>(queueSize), Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Autowired(required = false)
    private Searcher searcher;

    @Autowired
    private WebLogDao webLogDao;

    @Autowired
    private GoodsService goodsService;


    @Override
    public Response<Boolean> index(final String orderNumber) {
        try {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Stopwatch watch = Stopwatch.createStarted();
                        //初始化searchOrder
                        WebOrder order = searcher.get(WebOrder.class, orderNumber);
                        if (order == null) {
                            order = new WebOrder();
                            order.setId(orderNumber);
                            order.setOrderNumber(orderNumber);
                        }

                        //查询goods info
                        GoodsPurchaseInfo goodsPurchaseInfo = webLogDao.queryPurchaseRecordByNumber(orderNumber);
                        Response<Goods> goodsResponse = goodsService.queryOneGoodsById(goodsPurchaseInfo.getGoodsId());
                        if (!goodsResponse.isSuccess()) {
                            logger.error("failed to query goods,orderNumber:{},cause:{}", orderNumber, goodsResponse.getErr());
                            return;
                        }

                        //构建webOrder
                        Goods goods = goodsResponse.getData();
                        WebGoods webGoods = new WebGoods();
                        webGoods.setId(goods.getId());
                        webGoods.setName(goods.getName());
                        webGoods.setCount(goods.getCount());
                        webGoods.setPrice(IntMath.checkedMultiply(goods.getPrice().intValue(), 100));
                        webGoods.setFlag(goods.getOsk() == 0 ? false : true);
                        webGoods.setRemark(goods.getRemark());

                        order.setGoods(webGoods);
                        order.setCount(goodsPurchaseInfo.getCount());
                        order.setAmt(IntMath.checkedMultiply(goodsPurchaseInfo.getMonetary().intValue(), 100));
                        order.setCity(goodsPurchaseInfo.getCity());
                        order.setPlaceTime(Dates.toDate(goodsPurchaseInfo.getPlaceTime()));
                        order.setResult(goodsPurchaseInfo.getSuccess() ? "成功" : "失败");

                        boolean indexed = searcher.index(order);
                        if (!indexed) {
                            logger.warn("can't save new index [{}], maybe is updating.", order.getOrderNumber());
                        }

                        logger.info("index order (orderNumber={}),spend:{}ms", orderNumber, watch.elapsed(TimeUnit.MILLISECONDS));
                    } catch (Exception e) {
                        logger.error("failed to index order({}), cause: {}",
                                orderNumber, Throwables.getStackTraceAsString(e));
                    }
                }
            });
            return Response.ok();
        } catch (Exception e) {
            logger.error("failed to index order({}), cause: {}",
                    orderNumber, Throwables.getStackTraceAsString(e));
            return Response.notOk(Messages.getString("search.index.failed"));
        }
    }

    @Override
    public Response<WebOrder> queryByOrderNumber(String orderNumber) {

        try {
            return Response.ok(searcher.get(WebOrder.class, orderNumber));
        } catch (Exception e) {
            logger.error("failed to query order({}), cause: {}", orderNumber, Throwables.getStackTraceAsString(e));
            return Response.notOk(Messages.getString("search.query.failed"));
        }

    }

    @Override
    public Response<Paging<WebOrder>> search(Integer pageNo, Integer pageSize, String st, String et, String srName, String srValue) {

        try {
            QueryBuilder qb = buildAllQuery(st, et, srName, srValue);
            queryPage(qb, pageNo, pageSize);
            //结果集排序
            qb.order("placeTime", OrderType.DESC);

            Paging<WebOrder> pagingData = searcher.search(WebOrder.class, qb.build());

            List<WebOrder> searchOrders = pagingData.getData();
            if (pagingData.getTotal() > 0) {

                pagingData = new Paging<WebOrder>(pagingData.getTotal(), searchOrders);
            }

            return Response.ok(pagingData);
        } catch (Exception e) {
            logger.error("failed to search orders, cause: {}", Throwables.getStackTraceAsString(e));
            return Response.notOk(Messages.getString("search.query.failed"));
        }

    }

    private QueryBuilder buildAllQuery(String st, String et, String srName, String srValue) {
        QueryBuilder qb = QueryBuilder.newBuilder();

        //关键字搜索
        if (!Strings.isNullOrEmpty(srName) && !Strings.isNullOrEmpty(srValue)) {
            if ("orderNumber".equals(srName)) {
                // 订单号
                qb.like(srName, srValue);
            } else if ("goods".equals(srName)) {
                // 物品名称
                qb.and("goods.name", srValue);
            } else {
                // 字段查询，webOrder中的字段
                qb.and(srName, srValue);
            }
        }

        //日期搜索
        queryDateRange(qb, st, et);

        return qb;
    }


    @Override
    public Response<Long> count(String st, String et, String srName, String srValue) {
        try {
            QueryBuilder qb = buildAllQuery(st, et, srName, srValue);

            return Response.ok(searcher.count(WebOrder.class, qb.build()));
        } catch (Exception e) {
            logger.error("failed to count order, cause: {}", Throwables.getStackTraceAsString(e));
            return Response.notOk(Messages.getString("search.query.failed"));
        }
    }

    private void queryDateRange(QueryBuilder qb, String st, String et) {
        Date sdate = getStartDate(st);
        Date edate = getEndDate(et);
        qb.range("placeTime", sdate.getTime(), edate.getTime());
    }

    private Date getStartDate(String st) {
        if (Strings.isNullOrEmpty(st)) {
            String today = DateTimeFormat.forPattern("yyyy-MM-dd").print(DateTime.now());
            return Dates.toDate(today, "yyyy-MM-dd");
        } else {
            if (st.length() == 10) {
                return Dates.toDate(st, "yyyy-MM-dd");
            }
            return Dates.toDate(st);
        }
    }

    private Date getEndDate(String et) {
        if (Strings.isNullOrEmpty(et)) {
            String today = DateTimeFormat.forPattern("yyyy-MM-dd").print(DateTime.now());
            return DateTime.parse(today).plusDays(1).toDate();
        } else {
            if (et.length() == 10) {
                return DateTime.parse(et).plusDays(1).toDate();
            }
            return Dates.toDate(et);

        }
    }

    private void queryPage(QueryBuilder qb, Integer pageNo, Integer pageSize) {
        Page page = new Page(pageNo, pageSize, Integer.MAX_VALUE);
        qb.offsetAndLimit(page.getOffset(), page.getLimit());
    }

}
