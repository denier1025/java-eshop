package com.webprojectpattern.eshop.dao.exception;

public class DaoBusinessException extends DaoException {

    public DaoBusinessException(String msg) {
        super(msg);
    }

    public DaoBusinessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
