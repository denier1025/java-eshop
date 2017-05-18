package com.webprojectpattern.eshop.dao.exception;

public class TheUsernameOrEmailIsAlreadyExist extends DaoBusinessException {

    public TheUsernameOrEmailIsAlreadyExist(String msg) {
        super(msg);
    }

    public TheUsernameOrEmailIsAlreadyExist(String msg, Throwable cause) {
        super(msg, cause);
    }
}