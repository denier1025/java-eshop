package com.webprojectpattern.eshop.dao.exception;

public class NoSuchEntityException extends DaoBusinessException {

    public NoSuchEntityException(String msg) {
        super(msg);
    }

    public NoSuchEntityException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
