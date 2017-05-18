package com.webprojectpattern.eshop.dao.exception;

public class TheUsernameOrPasswordIsIncorrect extends DaoBusinessException {

    public TheUsernameOrPasswordIsIncorrect(String msg) {
        super(msg);
    }

    public TheUsernameOrPasswordIsIncorrect(String msg, Throwable cause) {
        super(msg, cause);
    }
}
