package com.webprojectpattern.eshop.system.validator;

import com.webprojectpattern.eshop.entity.User;

import java.util.Map;

public interface UserValidator {

    Map<String, String> validate(User user);
}
