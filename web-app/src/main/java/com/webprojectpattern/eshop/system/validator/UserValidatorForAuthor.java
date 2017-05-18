package com.webprojectpattern.eshop.system.validator;

import com.webprojectpattern.eshop.entity.User;

import java.util.HashMap;
import java.util.Map;

public class UserValidatorForAuthor implements UserValidator {

    private static final String NEEDS_TO_FILL = "This_field_needs_to_be_fill";
    private static final String INCORRECT_USERNAME = "Incorrect_username";
    private static final String INCORRECT_PASSWORD = "Incorrect_password";

    @Override
    public Map<String, String> validate(User user) {

        Map<String, String> errorMap = new HashMap<>();

        validateUsername(user.getUsername(), errorMap);

        validatePassword(user.getPassword(), errorMap);

        return errorMap;
    }

    public void validateUsername(String username, Map<String, String> errorMap) {

        if(username != null && !username.equals("")) {
            if (!(username.length() >= 3 && username.length() <= 25)) {
                errorMap.put("username", INCORRECT_USERNAME);
                return;
            }
            errorMap.put("username", username);
        } else {
            errorMap.put("username", NEEDS_TO_FILL);
        }
    }

    public void validatePassword(String password, Map<String, String> errorMap) {

        if(password != null && !password.equals("")) {
            if (!(password.length() >= 5 && password.length() <= 25)) {
                errorMap.put("password", INCORRECT_PASSWORD);
                return;
            }
            errorMap.put("password", password);
        } else {
            errorMap.put("password", NEEDS_TO_FILL);
        }
    }
}
