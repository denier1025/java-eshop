package com.webprojectpattern.eshop.dao.transactionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class c3p0DataSource extends BaseDataSource {

    private static final Logger logger = LogManager.getLogger(c3p0DataSource.class);

    private String driverClass;
    private String jdbcUrl;
    private String user;
    private String password;
    private ComboPooledDataSource dataSource;

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void init() throws SQLException {
        try{
            logger.debug("Trying to initialize c3p0ConnectionPool...");
            dataSource = new ComboPooledDataSource();
            logger.debug("c3p0ConnectionPool was initialized!");
            logger.debug("Setting the main properties for DB...");
            dataSource.setDriverClass(driverClass);
            dataSource.setJdbcUrl(jdbcUrl);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            logger.debug("The main properties for DB were set!");
            logger.debug("Setting the properties for c3p0ConnectionPool...");
            dataSource.setMinPoolSize(3);
            dataSource.setAcquireIncrement(1);
            dataSource.setMaxPoolSize(16);
            dataSource.setMaxStatements(64);
            dataSource.setMaxIdleTime(30);
            logger.debug("The properties for c3p0ConnectionPool were set!");
        } catch (PropertyVetoException e) {
            logger.warn("Exception in the init method c3p0DataSource");
            throw new SQLException("Exception during configuring c3p0", e);
        }
    }

    public void close() throws SQLException {
        logger.debug("Closing c3p0ConnectionPool...");
        dataSource.close();
    }
}
