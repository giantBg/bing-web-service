package com.bingcore.web.util.log;

import com.bingcore.web.model.other.GoodsPurchaseInfo;
import com.bingcore.web.repository.WebLogDao;
import com.bingcore.web.service.SearchService;
import com.bingcore.web.util.SpringContextUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.ishansong.common.date.Dates;
import com.ishansong.common.model.Response;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * Created by xubing on 16/5/23.
 */
public class PurchaseGoodsHandler extends BaseLogHander {


    private WebLogDao webLogDao = SpringContextUtil.getBean(WebLogDao.class);
    private SearchService searchService = SpringContextUtil.getBean(SearchService.class);


    public Response<Object> exec(Map<String, String> params) {

        //可以直接实现逻辑或者使用多线程实现eventDeal
        eventDeal(new GoodsPurchaseTask(params));
        return Response.ok();

    }

    class GoodsPurchaseTask implements Runnable {

        private Map<String, String> params;


        GoodsPurchaseTask(Map<String, String> params) {
            this.params = params;
        }

        @Override
        public void run() {

            String orderNumber = MapUtils.getString(params, "orderNumber");
            Integer goodsId = MapUtils.getInteger(params, "goodsId");
            String city = MapUtils.getString(params, "city");
            Integer count = MapUtils.getInteger(params, "count");
            Double consumeAmt = MapUtils.getDouble(params, "amt");
            Long placeTime = MapUtils.getLong(params, "placeTime");
            Boolean isSuccess = MapUtils.getBoolean(params, "result");

            Preconditions.checkArgument(!Strings.isNullOrEmpty(orderNumber), "orderNumber is null!");
            Preconditions.checkArgument(goodsId != null, "Goods Id is null!");
            Preconditions.checkArgument(count != null && count > 0, "purchase Count is illegal!");

            String orderTime = Dates.now("yyyy-MM-dd HH:mm:ss");
            if (placeTime != null) {
                orderTime = Dates.format(placeTime);
            }

            try {
                GoodsPurchaseInfo goodsPurchaseInfo = new GoodsPurchaseInfo(orderNumber, goodsId, city, count, consumeAmt, orderTime, isSuccess);
                webLogDao.savePurchaseInfo(goodsPurchaseInfo);
                logger.info("save mongodb data successfully, orderNumber:{}", orderNumber);

                //notify es
                searchService.index(orderNumber);

            } catch (Exception e) {
                logger.error("PurchaseGoodsHandler: fail to save purchase record. orderNumber={},errMsg={}", orderNumber, Throwables.getStackTraceAsString(e));
                e.printStackTrace();
            }

        }
    }
}
