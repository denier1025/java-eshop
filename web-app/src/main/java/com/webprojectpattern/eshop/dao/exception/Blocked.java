package com.webprojectpattern.eshop.dao.exception;

public class Blocked extends DaoBusinessException {

    public Blocked(String msg) {
        super(msg);
    }

    public Blocked(String msg, Throwable cause) {
        super(msg, cause);
    }
}