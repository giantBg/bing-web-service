package com.bingcore.web.repository;

import com.bingcore.web.model.db.MapChartData;
import com.bingcore.web.util.memcached.MemcachedClient;
import com.bingcore.web.util.memcached.MemcachedKeys;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by xubing on 16/5/11.
 */
@Repository
public class MapChartDao extends BaseDao {

    /**
     * 查询地图数据信息
     *
     * @return
     */
    public List<MapChartData> queryMapData() {
        StringBuilder selecting = new StringBuilder("select * from map_chart_data ");

        return webJdbc.query(selecting.toString(), new BeanPropertyRowMapper<MapChartData>(MapChartData.class));
    }


    /**
     * 根据城市城市和商品查询地图数据
     *
     * @param city  城市
     * @param goods 商品名称
     * @return
     */
    public MapChartData queryMapDataByCityAndGoods(String city, String goods) {
        StringBuilder sql = new StringBuilder("select * from map_chart_data")
                .append(" where goods=? and city= ?");

        List<MapChartData> MapData = webJdbc.query(sql.toString(), new Object[]{goods, city}, new BeanPropertyRowMapper<MapChartData>(MapChartData.class));

        return MapData.size() > 0 ? MapData.get(0) : null;
    }


    /**
     * 更新商品数量
     *
     * @param id
     * @return
     */
    public Boolean updateMapDataCount(final Integer id, final Integer buyCount) {
        final StringBuilder sql = new StringBuilder("update map_chart_data set count=count+?,utime=? where id=?");

        //clean cache
        MemcachedClient.delete(MemcachedKeys.queryAllMapDataKey);

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
     * 存储地图数据结果
     *
     * @param mapChartData
     * @return
     */
    public Boolean saveMapData(final MapChartData mapChartData) {
        final StringBuilder sql = new StringBuilder("insert into map_chart_data(goods,city,count,ctime,utime) values(?, ?, ?, ?, ?)");

        //clean cache
        MemcachedClient.delete(MemcachedKeys.queryAllMapDataKey);

        return webJdbc.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement stmt = conn.prepareStatement(sql.toString());
                stmt.setString(1, mapChartData.getGoods());
                stmt.setString(2, mapChartData.getCity());
                stmt.setInt(3, mapChartData.getCount());
                stmt.setLong(4, System.currentTimeMillis());
                stmt.setLong(5, System.currentTimeMillis());
                return stmt;
            }
        }) > 0 ? Boolean.TRUE : Boolean.FALSE;
    }


}
