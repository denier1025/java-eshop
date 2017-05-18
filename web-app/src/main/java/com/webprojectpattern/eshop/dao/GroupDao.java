package com.webprojectpattern.eshop.dao;

import com.webprojectpattern.eshop.dao.exception.NoSuchEntitiesException;
import com.webprojectpattern.eshop.entity.Group;
import com.webprojectpattern.eshop.entity.Product;

import java.sql.SQLException;
import java.util.List;

public interface GroupDao {

    List<Group> selectAll() throws SQLException;

    Boolean insertGroup(Group group) throws SQLException;

    Boolean updateGroup(Group group) throws SQLException;

    List<Product> selectByGroupName(String groupName) throws SQLException;
}
