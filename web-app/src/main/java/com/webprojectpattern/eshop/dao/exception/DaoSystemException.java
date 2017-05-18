package com.webprojectpattern.eshop.dao.exception;

public class DaoSystemException extends DaoException {

    public DaoSystemException(String msg) {
        super(msg);
    }

    public DaoSystemException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
