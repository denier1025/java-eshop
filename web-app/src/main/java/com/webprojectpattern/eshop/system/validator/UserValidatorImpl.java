package com.webprojectpattern.eshop.system.validator;

import com.webprojectpattern.eshop.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidatorImpl implements UserValidator {

    private static final String NEEDS_TO_FILL = "This_field_needs_to_be_fill";
    private static final String INCORRECT_USERNAME = "Incorrect_username";
    private static final String INCORRECT_PASSWORD = "Incorrect_password";
    private static final String INCORRECT_EMAIL = "Incorrect_email";

    private Pattern pattern;
    private Matcher matcher;
    private String usernamePattern;
    private String passwordPattern;
    private String emailPattern;

    public void setUsernamePattern(String usernamePattern) {
        this.usernamePattern = usernamePattern;
    }
    public void setPasswordPattern(String passwordPattern) {
        this.passwordPattern = passwordPattern;
    }
    public void setEmailPattern(String emailPattern) {
        this.emailPattern = emailPattern;
    }

    @Override
    public Map<String, String> validate(User user) {

        Map<String, String> errorMap = new HashMap<>();

        validateUsername(user.getUsername(), errorMap, usernamePattern);

        validatePassword(user.getPassword(), errorMap, passwordPattern);

        validateEmail(user.getEmail(), errorMap, emailPattern);

        return errorMap;
    }

    public void validateUsername(String username, Map<String, String> errorMap, String usernamePattern) {

        if(username != null && !username.equals("")) {
            pattern = Pattern.compile(usernamePattern);
            matcher = pattern.matcher(username);
            if (!matcher.matches()) {
                errorMap.put("username", INCORRECT_USERNAME);
                return;
            }
            errorMap.put("username", username);
        } else {
            errorMap.put("username", NEEDS_TO_FILL);
        }
    }

    public void validatePassword(String password, Map<String, String> errorMap, String passwordPattern) {

        if(password != null && !password.equals("")) {
            pattern = Pattern.compile(passwordPattern);
            matcher = pattern.matcher(password);
            if (!matcher.matches()) {
                errorMap.put("password", INCORRECT_PASSWORD);
                return;
            }
            errorMap.put("password", password);
        } else {
            errorMap.put("password", NEEDS_TO_FILL);
        }
    }

    public void validateEmail(String email, Map<String, String> errorMap, String emailPattern) {

        if(email != null && !email.equals("")) {
            pattern = Pattern.compile(emailPattern);
            matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                errorMap.put("email", INCORRECT_EMAIL);
                return;
            }
            errorMap.put("email", email);
        } else {
            errorMap.put("email", NEEDS_TO_FILL);
        }
    }

}
