package com.bingcore.web.util.nsq;

import com.bingcore.web.consts.SysConst;
import com.bingcore.web.model.other.GoodsMessage;
import com.bingcore.web.service.GoodsService;
import com.google.common.base.Strings;
import com.ishansong.common.model.Response;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xubing on 16/5/11.
 *
 * @Author xubing
 * @DateTime 2016-05-11 16:14
 */
public class NsqMsgConsumer extends MsgConsumer<GoodsMessage> {


    @Autowired
    GoodsService goodsService;

    public NsqMsgConsumer(MsgConfig config) {
        super(config, SysConst.WEB_TOPIC);
    }

    @Override
    protected void onMessage(GoodsMessage msg) {

        logger.info("nsq consumer: receive Message :{}", msg);

        //precondition check
        if (msg == null) {
            logger.error("msg {} is null or empty !", msg);
            return;
        }

        if (msg.getGoods() == null || Strings.isNullOrEmpty(msg.getCity())) {
            logger.error("order params is null or empty, goods:{},city:{}", msg.getGoods(), msg.getCity());
            return;
        }

        if (msg.getCount() != null && NumberUtils.isNumber(msg.getCount().toString())) {
            //start to place order
            Response<Boolean> placeResult = goodsService.confirmPlaceOrder(msg.getGoods(), msg.getCity(), msg.getCount());

            logger.info("nsq consumer: place order finished ,result:" + placeResult.getData());
        } else {
            logger.error("order purchase count is not a number {}", msg.getCount());
        }

    }
}
