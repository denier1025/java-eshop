package com.webprojectpattern.eshop.dao.transactionManager;

import com.webprojectpattern.eshop.dao.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class TransactionManagerImpl extends BaseDataSource implements TransactionManager {

    private static final Logger logger = LogManager.getLogger(TransactionManagerImpl.class);

    private static final ThreadLocal<Connection> connectionThreadHolder = new ThreadLocal<>();
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> T doInTransaction(Callable<T> unitOfWork) throws Exception {
        logger.debug("Trying to get connection");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was initialized: " + conn);
        try{
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);
            connectionThreadHolder.set(conn);
            logger.debug("Connection was set into the ThreadLocal variable: " + conn);
            logger.debug("Waiting for unitOfWork will done...");
            T result = unitOfWork.call();
            logger.debug("unitOfWork is done!");
            conn.commit();
            return result;
        } catch (Exception e) {
            logger.debug("Exception in the txManager!", e);
            JdbcUtils.rollBack(conn);
            throw e;
        } finally {
            connectionThreadHolder.remove();
            logger.debug("Connection from the ThreadLocal variable was removed");
            conn.close();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        logger.debug("Getting connection from the ThreadLocal variable...");
        Connection conn = connectionThreadHolder.get();
        logger.debug("Connection from the ThreadLocal variable: " + conn);
        if(conn != null) {
            return conn;
        } else {
            throw new IllegalStateException("'getConnection()' was invoked outside the TransactionManager!");
        }
    }
}
