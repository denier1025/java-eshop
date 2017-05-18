package com.webprojectpattern.eshop.dao;

import com.webprojectpattern.eshop.dao.exception.Blocked;
import com.webprojectpattern.eshop.dao.exception.NoSuchEntityException;
import com.webprojectpattern.eshop.entity.Product;

import java.io.IOException;
import java.sql.SQLException;

public interface ProductDao {

    Product selectByProductName(String productName) throws Blocked, NoSuchEntityException, SQLException, IOException;

    Boolean insertProduct(Product product) throws SQLException, IOException;

    Boolean updateProduct(Product product, int groupId) throws SQLException;

    Integer blockById(int id) throws NoSuchEntityException, SQLException;

    public Integer unblockById(int id) throws NoSuchEntityException, SQLException;
}
