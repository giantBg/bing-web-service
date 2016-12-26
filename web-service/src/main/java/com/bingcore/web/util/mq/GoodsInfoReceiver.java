package com.bingcore.web.util.mq;

import com.bingcore.web.model.other.GoodsInfoMessage;
import com.bingcore.web.repository.GoodsDao;
import com.google.common.base.Throwables;

import javax.annotation.Resource;

/**
 * Created by xubing on 16/09/06.
 *
 * @Author xubing
 * @DateTime 2016-09-06 22:14
 */
public class GoodsInfoReceiver extends MessageReceiver<GoodsInfoMessage> {

    @Resource
    private GoodsDao goodsDao;

    @Override
    protected void exec(GoodsInfoMessage msg) {
        try {
            logger.info("exec modify goods info (goodsInfo={}), result:{}", msg.getGoods(),
                    goodsDao.updateGoodsInfo(msg.getGoods()));
        } catch (Exception e) {
            logger.error("failed to modify goodsInfo (goodsInfo={}),cause:{}", msg.getGoods(), Throwables.getStackTraceAsString(e));
            e.printStackTrace();
        }
    }

}
