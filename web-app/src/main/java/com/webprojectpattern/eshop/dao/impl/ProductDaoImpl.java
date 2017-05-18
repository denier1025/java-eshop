package com.webprojectpattern.eshop.dao.impl;

import com.webprojectpattern.eshop.dao.ProductDao;
import com.webprojectpattern.eshop.dao.exception.Blocked;
import com.webprojectpattern.eshop.dao.exception.NoSuchEntityException;
import com.webprojectpattern.eshop.dao.utils.JdbcUtils;
import com.webprojectpattern.eshop.entity.Product;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;

public class ProductDaoImpl implements ProductDao {

    private static final Logger logger = LogManager.getLogger(ProductDaoImpl.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Product selectByProductName(String productName) throws Blocked, NoSuchEntityException, SQLException, IOException {
        logger.debug("Trying to get connection...");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was got!");
        PreparedStatement prStmt = null;
        ResultSet rs = null;
        try{
            logger.debug("Sending SQL Query to the DB...");
            prStmt = conn.prepareStatement("SELECT `id`, `productName`, `count`, `color`, `size`, `price`, `description`, `blocked`, `group_id`, `img` FROM products WHERE productName = ?");
            prStmt.setString(1, productName);
            rs = prStmt.executeQuery();
            logger.debug("SQL Query is successful done!");
            Product result = new Product();
            if(rs.next()) {
                logger.debug("Checking product for block...");
                if(rs.getBoolean("blocked")) {
                    throw new Blocked("Blocked");
                }
                logger.debug("Product is not blocked!");
                result.setId(rs.getInt("id"));
                result.setProductName(rs.getString("productName"));
                result.setCount(rs.getInt("count"));
                result.setColor(rs.getString("color"));
                result.setSize(rs.getString("size"));
                result.setPrice(rs.getInt("price"));
                result.setDescription(rs.getString("description"));
                result.setBlocked(rs.getBoolean("blocked"));
                result.setGroupId(rs.getInt("group_id"));
                result.setInputStream(rs.getBinaryStream("img"));

                return result;
            } else {
                logger.debug("No such product! Exception!");
                throw new NoSuchEntityException("Incorrect productName!");
            }
        } finally {
            logger.debug("Closing rs&prStmt...");
            JdbcUtils.close(rs);
            JdbcUtils.close(prStmt);
        }
    }

    @Override
    public Boolean insertProduct(Product product) throws SQLException, IOException {
        logger.debug("Trying to get connection...");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was got!");
        PreparedStatement prStmt = null;
        try {
            logger.debug("Sending SQL Query for INSERT to the DB...");
            prStmt = conn.prepareStatement("INSERT INTO products (`productName`, " +
                    "`count`, " +
                    "`color`, " +
                    "`size`, " +
                    "`price`, " +
                    "`description`, " +
                    "`blocked`, " +
                    "`group_id`, " +
                    "`img`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            prStmt.setString(1, product.getProductName());
            prStmt.setInt(2, product.getCount());
            prStmt.setString(3, product.getColor());
            prStmt.setString(4, product.getSize());
            prStmt.setInt(5, product.getPrice());
            prStmt.setString(6, product.getDescription());
            prStmt.setBoolean(7, product.isBlocked());
            prStmt.setInt(8, product.getGroupId());
            prStmt.setBinaryStream(9, product.getInputStream());
            boolean result = prStmt.execute();
            logger.debug("SQL Query is successful done!");
            return result;
        } finally {
            logger.debug("Closing prStmt...");
            JdbcUtils.close(prStmt);
        }
    }

    @Override
    public Integer blockById(int id) throws NoSuchEntityException, SQLException {
        logger.debug("Trying to get connection...");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was got!");
        PreparedStatement prStmt = null;
        try{
            logger.debug("Sending SQL Query to the DB...");
            prStmt = conn.prepareStatement("UPDATE products SET blocked = true WHERE id = ?");
            prStmt.setInt(1, id);
            int result = prStmt.executeUpdate();
            logger.debug("SQL Query is successful done!");
            if(result == 0) {
                logger.debug("No such product in the DB! Exception!");
                throw new NoSuchEntityException("Product is not exist");
            }
            return result;
        } finally {
            logger.debug("Closing prStmt...");
            JdbcUtils.close(prStmt);
        }
    }

    @Override
    public Integer unblockById(int id) throws NoSuchEntityException, SQLException {
        logger.debug("Trying to get connection...");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was got!");
        PreparedStatement prStmt = null;
        try{
            logger.debug("Sending SQL Query to the DB...");
            prStmt = conn.prepareStatement("UPDATE products SET blocked = false WHERE id = ?");
            prStmt.setInt(1, id);
            int result = prStmt.executeUpdate();
            logger.debug("SQL Query is successful done!");
            if(result == 0) {
                logger.debug("No such product in the DB! Exception!");
                throw new NoSuchEntityException("Product is not exist");
            }
            return result;
        } finally {
            logger.debug("Closing prStmt...");
            JdbcUtils.close(prStmt);
        }
    }

    @Override
    public Boolean updateProduct(Product product, int groupId) throws SQLException {
        logger.debug("Trying to get connection...");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was got!");
        PreparedStatement prStmt = null;
        try {
            logger.debug("Sending SQL Query for UPDATE to the DB...");
            prStmt = conn.prepareStatement("UPDATE products SET `productName` = ?, " +
                    "`count` = ?, " +
                    "`color` = ?, " +
                    "`size` = ?, " +
                    "`price` = ?, " +
                    "`description` = ?, " +
                    "`blocked` = ? " +
                    "`group_id` = ?, " +
                    "`img` = ? WHERE id = ?");
            prStmt.setString(1, product.getProductName());
            prStmt.setInt(2, product.getCount());
            prStmt.setString(3, product.getColor());
            prStmt.setString(4, product.getSize());
            prStmt.setInt(5, product.getPrice());
            prStmt.setString(6, product.getDescription());
            prStmt.setBoolean(7, product.isBlocked());
            prStmt.setInt(8, groupId);
            prStmt.setBinaryStream(9, product.getInputStream());
            prStmt.setInt(10, product.getId());
            boolean result = prStmt.execute();
            logger.debug("SQL Query is successful done!");
            return result;
        } finally {
            logger.debug("Closing prStmt...");
            JdbcUtils.close(prStmt);
        }
    }
}
