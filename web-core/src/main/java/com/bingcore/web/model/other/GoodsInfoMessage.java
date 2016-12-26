package com.bingcore.web.model.other;

import com.bingcore.web.model.db.Goods;
import com.ishansong.activemq.message.DefaultMessage;

import java.io.Serializable;

/**
 * 商品修改消息
 * Created by xubing on 16/09/05.
 */
public class GoodsInfoMessage implements DefaultMessage, Serializable {

    private static final long serialVersionUID = 4655582024410511554L;

    private Goods goods;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
