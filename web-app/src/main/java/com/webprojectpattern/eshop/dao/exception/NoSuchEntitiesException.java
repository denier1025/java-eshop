package com.webprojectpattern.eshop.dao.exception;

public class NoSuchEntitiesException extends DaoBusinessException {

    public NoSuchEntitiesException(String msg) {
        super(msg);
    }

    public NoSuchEntitiesException(String msg, Throwable cause) {
        super(msg, cause);
    }
}