package com.webprojectpattern.eshop.dao.impl;

import com.webprojectpattern.eshop.dao.UserDao;
import com.webprojectpattern.eshop.dao.exception.*;
import com.webprojectpattern.eshop.dao.utils.JdbcUtils;
import com.webprojectpattern.eshop.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User selectByUsernameAndPassword(User user) throws Banned, TheUsernameOrPasswordIsIncorrect, SQLException {
        logger.debug("Trying to get connection in UserDao...");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was got!");
        PreparedStatement prStmt = null;
        ResultSet rs = null;
        try{
            logger.debug("Sending SQL Query to the DB...");
            prStmt = conn.prepareStatement("SELECT id, username, password, email, banned, status FROM users WHERE username = ? AND password = ?");
            prStmt.setString(1, user.getUsername());
            prStmt.setString(2, user.getPassword());
            rs = prStmt.executeQuery();
            logger.debug("SQL Query is successful done!");
            User result = new User();
            if(rs.next()) {
                logger.debug("Checking user for banned...");
                if(rs.getBoolean("banned")) {
                    throw new Banned("Banned");
                }
                logger.debug("User is not  banned!");
                result.setId(rs.getInt("id"));
                result.setUsername(rs.getString("username"));
                result.setPassword(rs.getString("password"));
                result.setEmail(rs.getString("email"));
                result.setStatus(rs.getString("status"));
                return result;
            } else {
                logger.debug("No such username or password in the DB! Exception!");
                throw new TheUsernameOrPasswordIsIncorrect("Incorrect username or password");
            }
        } finally {
            logger.debug("Closing rs&prStmt...");
            JdbcUtils.close(rs);
            JdbcUtils.close(prStmt);
        }
    }

    @Override
    public Boolean insert(User user) throws TheUsernameOrEmailIsAlreadyExist, SQLException {
        logger.debug("Trying to get connection in UserDao...");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was got!");
        PreparedStatement prStmt = null;
        try {
            logger.debug("Checking username for duplicates...");
            if (existsWithUsername(conn, user.getUsername())) {
                throw new TheUsernameOrEmailIsAlreadyExist("Username '" + user.getUsername() + "' is already exist");
            }
            logger.debug("Checking email for duplicates...");
            if (existsWithEmail(conn, user.getEmail())) {
                throw new TheUsernameOrEmailIsAlreadyExist("Email '" + user.getEmail() + "' is already exist");
            }
            logger.debug("Sending SQL Query to the DB...");
            prStmt = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)");
            prStmt.setString(1, user.getUsername());
            prStmt.setString(2, user.getPassword());
            prStmt.setString(3, user.getEmail());
            boolean result = prStmt.execute();
            logger.debug("SQL Query is successful done!");
            return result;
        } finally {
            logger.debug("Closing prStmt...");
            JdbcUtils.close(prStmt);
        }
    }

    @Override
    public Boolean changePassword(User user, String password) throws SQLException {
        logger.debug("Trying to get connection in UserDao...");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was got!");
        PreparedStatement prStmt = null;
        try {
            logger.debug("Sending SQL Query to the DB...");
            prStmt = conn.prepareStatement("UPDATE users SET password = ? WHERE username = ?");
            prStmt.setString(1, password);
            prStmt.setString(2, user.getUsername());
            boolean result = prStmt.execute();
            logger.debug("SQL Query is successful done!");
            return result;
        } finally {
            logger.debug("Closing prStmt...");
            JdbcUtils.close(prStmt);
        }
    }

    @Override
    public Integer banByUsername(String username) throws NoSuchEntityException, SQLException {
        logger.debug("Trying to get connection in UserDao...");
        Connection conn = dataSource.getConnection();
        logger.debug("Connection was got!");
        PreparedStatement prStmt = null;
        try{
            logger.debug("Sending SQL Query to the DB...");
            prStmt = conn.prepareStatement("UPDATE users SET banned = true WHERE username = ?");
            prStmt.setString(1, username);
            int result = prStmt.executeUpdate();
            logger.debug("SQL Query is successful done!");
            if(result == 0) {
                logger.debug("No such user in the DB! Exception!");
                throw new NoSuchEntityException("User is not exist");
            }
            return result;
        } finally {
            logger.debug("Closing prStmt...");
            JdbcUtils.close(prStmt);
        }
    }

    private boolean existsWithUsername(Connection conn, String username) throws SQLException {
        logger.debug("Sending SQL Query to the DB...");
        PreparedStatement ps = conn.prepareStatement("SELECT username FROM users WHERE username = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        logger.debug("SQL Query is successful done!");
        return rs.next();
    }

    private boolean existsWithEmail(Connection conn, String email) throws SQLException {
        logger.debug("Sending SQL Query to the DB...");
        PreparedStatement ps = conn.prepareStatement("SELECT email FROM users WHERE email = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        logger.debug("SQL Query is successful done!");
        return rs.next();
    }
}
