package com.bingcore.web.repository;

import com.bingcore.web.model.db.Goods;
import com.bingcore.web.model.db.MapChartData;
import com.bingcore.web.util.memcached.MapChartDaoCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by xubing on 16/5/11.
 */
@Repository
public class GoodsDao extends BaseDao {

    @Autowired
    MapChartDao mapChartDao;

    @Autowired
    MapChartDaoCache mapChartDaoCache;

    /**
     * 查询所有商品信息
     *
     * @return
     */
    public List<Goods> queryAllGoodsInfo() {
        StringBuilder selecting = new StringBuilder("select * from goods ");

        return webJdbc.query(selecting.toString(), new BeanPropertyRowMapper<Goods>(Goods.class));
    }

    /**
     * 根据城市id查询商品信息
     *
     * @return
     */
    public Goods queryGoodsById(Integer goodId) {
        StringBuilder sql = new StringBuilder("select * from goods")
                .append(" where osk=0 and id= ?");

        List<Goods> goods = webJdbc.query(sql.toString(), new Object[]{goodId}, new BeanPropertyRowMapper<Goods>(Goods.class));

        return goods.size() > 0 ? goods.get(0) : null;
    }


    /**
     * 更新商品余量
     *
     * @param id
     * @return
     */
    public Boolean updateGoodsLeftCount(final Integer id, final Integer buyCount) {
        final StringBuilder sql = new StringBuilder("update goods set count = count-?,utime=? where id=?");

        return webJdbc.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(sql.toString());
                stmt.setInt(1, buyCount);
                stmt.setLong(2, System.currentTimeMillis());
                stmt.setInt(3, id);
                return stmt;
            }
        }) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 更新商品信息
     *
     * @param goods 商品实例
     * @return
     */
    public Boolean updateGoodsInfo(final Goods goods) {
        final StringBuilder sql = new StringBuilder("update goods set price=?, count=?, osk=?, utime=? where id=?");

        return webJdbc.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(sql.toString());
                stmt.setBigDecimal(1, goods.getPrice());
                stmt.setInt(2, goods.getCount());
                stmt.setInt(3, goods.getOsk());
                stmt.setLong(4, System.currentTimeMillis());
                stmt.setInt(5, goods.getId());
                return stmt;
            }
        }) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 确认下单
     *
     * @param goods 商品信息
     * @param city  城市
     * @param count 购买数量
     * @return
     */
    public Boolean confirmToPlaceOrder(final Goods goods, final String city, final Integer count) {
        return webTransaction.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus transactionStatus) {
                try {

                    //1. update goods left count
                    updateGoodsLeftCount(goods.getId(), count);

                    //2. update goods map data
                    MapChartData mapChartData = mapChartDaoCache.queryMapDataByParams(city, goods.getName());

                    if (mapChartData != null) {
                        mapChartDao.updateMapDataCount(mapChartData.getId(), count);
                    } else {
                        MapChartData mapData = new MapChartData();
                        mapData.setGoods(goods.getName());
                        mapData.setCity(city);
                        mapData.setCount(count);
                        mapChartDao.saveMapData(mapData);
                    }

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    transactionStatus.setRollbackOnly();
                }
                return false;
            }
        });
    }

}
