package com.webprojectpattern.eshop.dao.transactionManager;

import java.util.concurrent.Callable;

public interface TransactionManager {

    <T> T doInTransaction(Callable<T> unitOfWork) throws Exception;
}
