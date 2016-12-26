package com.bingcore.web.repository;

import com.bingcore.web.model.other.GoodsPurchaseInfo;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.ishansong.common.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xubing on 16/5/11.
 */
@Repository
public class WebLogDao {

    private final static Logger logger = LoggerFactory.getLogger("web");

    @Autowired
    private MongoTemplate mongoTemplate;

    private String getCollectionName() {
        return "purchase_records";
    }

    /**
     * 保存购买记录
     *
     * @param goodsPurchaseInfo 购买信息
     * @return
     */
    public void savePurchaseInfo(GoodsPurchaseInfo goodsPurchaseInfo) {
        mongoTemplate.save(goodsPurchaseInfo, getCollectionName());
    }

    /**
     * 查询购买记录
     *
     * @param orderNumber 订单单号
     * @return 购买记录
     */
    public GoodsPurchaseInfo queryPurchaseRecordByNumber(String orderNumber) {
        Criteria criteria = Criteria.where("orderNumber").is(orderNumber);
        return mongoTemplate.findOne(new Query(criteria), GoodsPurchaseInfo.class, getCollectionName());
    }

    /**
     * 查询客户购买记录
     *
     * @param offset    起始偏移
     * @param limit     分页大小
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @param srName    关键字名称
     * @param srValue   关键字值
     */
    public Paging<GoodsPurchaseInfo> pageQueryGoodsInfo(String startTime, String endTime, String srName, String srValue, Integer limit, Integer offset) {
        Criteria criteria = Criteria.where("placeTime").gte(startTime).lt(endTime);
        if (Objects.equal(srName, "goodsId") && !Strings.isNullOrEmpty(srValue)) {
            criteria.and("goodsId").is(Integer.parseInt(srValue));
        } else if (!Strings.isNullOrEmpty(srValue)) {
            criteria.and(srName).regex(".*?\\" + srValue + ".*");
        }

        logger.info("mongo search: srName={},srValue={}", srName, srValue);
        Long total = mongoTemplate.count(new Query(criteria), GoodsPurchaseInfo.class, getCollectionName());
        if (total == null || total <= 0L) {
            return Paging.empty();
        }

        List<GoodsPurchaseInfo> data = mongoTemplate.find(new Query(criteria).with(new Sort(Sort.Direction.DESC, "placeTime")).limit(limit).skip(offset), GoodsPurchaseInfo.class, getCollectionName());
        return new Paging<GoodsPurchaseInfo>(total, data);
    }


}
