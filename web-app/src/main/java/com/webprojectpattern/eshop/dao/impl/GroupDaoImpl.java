package com.webprojectpattern.eshop.dao.impl;

import com.webprojectpattern.eshop.dao.GroupDao;
import com.webprojectpattern.eshop.dao.exception.NoSuchEntitiesException;
import com.webprojectpattern.eshop.dao.utils.JdbcUtils;
import com.webprojectpattern.eshop.entity.Group;
import com.webprojectpattern.eshop.entity.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDaoImpl implements GroupDao {

    private static final Logger logger = LogManager.getLogger(GroupDaoImpl.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Group> selectAll() throws SQLException {
        logger.debug("Trying to get connection...");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was got!");
        Statement stmt = null;
        ResultSet rs = null;
        try{
            logger.debug("Trying to get statement...");
            stmt = conn.createStatement();
            logger.debug("Sending SQL Query to the DB...");
            rs = stmt.executeQuery("SELECT id, groupName, description FROM groups");
            logger.debug("SQL Query is successful done!");
            List<Group> result = new ArrayList<>();
            while(rs.next()) {
                result.add(new Group(rs.getInt("id"), rs.getString("groupName"), rs.getString("description")));
            }
            return result;
        } finally {
            logger.debug("Closing rs&stmt...");
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
    }

    @Override
    public Boolean updateGroup(Group group) throws SQLException {
        logger.debug("Trying to get connection...");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was got!");
        PreparedStatement prStmt = null;
        try {
            logger.debug("Sending SQL Query for UPDATE to the DB...");
            prStmt = conn.prepareStatement("UPDATE groups SET groupName = ?, " +
                    "description = ? WHERE id = ?");
            prStmt.setString(1, group.getGroupName());
            prStmt.setString(2, group.getDescription());
            prStmt.setInt(3, group.getId());
            boolean result = prStmt.execute();
            logger.debug("SQL Query is successful done!");
            return result;
        } finally {
            logger.debug("Closing prStmt...");
            JdbcUtils.close(prStmt);
        }
    }

    @Override
    public Boolean insertGroup(Group group) throws SQLException {
        logger.debug("Trying to get connection...");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was got!");
        PreparedStatement prStmt = null;
        try {
            logger.debug("Sending SQL Query for INSERT to the DB...");
            prStmt = conn.prepareStatement("INSERT INTO groups (groupName, description) VALUES (?, ?)");
            prStmt.setString(1, group.getGroupName());
            prStmt.setString(2, group.getDescription());
            boolean result = prStmt.execute();
            logger.debug("SQL Query is successful done!");
            return result;
        } finally {
            logger.debug("Closing prStmt...");
            JdbcUtils.close(prStmt);
        }
    }

    @Override
    public List<Product> selectByGroupName(String groupName) throws SQLException {
        logger.debug("Trying to get connection...");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was got!");
        PreparedStatement prStmt = null;
        ResultSet rs = null;
        try{
            logger.debug("Sending SQL Query to the DB...");
            prStmt = conn.prepareStatement("SELECT products.productName AS productName, " +
                    "products.price AS price, " +
                    "products.blocked AS blocked " +
                    "FROM products INNER JOIN groups ON products.group_id = groups.id WHERE groups.groupName = ?");
            prStmt.setString(1, groupName);
            rs = prStmt.executeQuery();
            logger.debug("SQL Query is successful done!");
            List<Product> result = new ArrayList<>();
            while(rs.next()) {
                logger.debug("Checking product for block...");
                if (rs.getBoolean("blocked")) {
                    continue;
                }
                logger.debug("Product is not blocked!");
                result.add(new Product(rs.getString("productName"), rs.getInt("price")));
            }
            return result;
        } finally {
            logger.debug("Closing rs&prStmt...");
            JdbcUtils.close(rs);
            JdbcUtils.close(prStmt);
        }
    }
}
