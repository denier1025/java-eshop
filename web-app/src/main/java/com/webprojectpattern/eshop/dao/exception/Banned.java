package com.webprojectpattern.eshop.dao.exception;

public class Banned extends DaoBusinessException {

    public Banned(String msg) {
        super(msg);
    }

    public Banned(String msg, Throwable cause) {
        super(msg, cause);
    }
}