package com.bingcore.web.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Created by bing on 16/03/28.
 */
public abstract class BaseDao {
    @Autowired
    @Qualifier("webJdbc")
    protected JdbcTemplate webJdbc;


    @Autowired
    @Qualifier("webServiceTransaction")
    protected TransactionTemplate webTransaction;

}
