package com.webprojectpattern.eshop.dao;

import com.webprojectpattern.eshop.dao.exception.*;
import com.webprojectpattern.eshop.entity.User;

import java.sql.SQLException;

public interface UserDao {

    User selectByUsernameAndPassword(User user) throws Banned, TheUsernameOrPasswordIsIncorrect, SQLException;

    Boolean insert(User user) throws TheUsernameOrEmailIsAlreadyExist, SQLException;

    Boolean changePassword(User user, String password) throws SQLException;

    Integer banByUsername(String username) throws NoSuchEntityException, SQLException;
}
