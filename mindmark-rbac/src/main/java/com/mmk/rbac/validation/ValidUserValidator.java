package com.mmk.rbac.validation;

import com.mmk.rbac.jpa.entity.UserEntity;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import org.springframework.stereotype.Component;

@Component
public class ValidUserValidator implements Validator {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_PATTERN = "^1[3-9]\\d{9}$";

    @Override
    public boolean supports(Class<?> clazz) {
        return UserEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserEntity user = (UserEntity) target;
        
        validateUsername(user, errors);
        validatePassword(user, errors);
        validateEmail(user, errors);
        validatePhone(user, errors);
    }

    private void validateUsername(UserEntity user, Errors errors) {
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            errors.rejectValue("userName", "user.username.empty", "用户名不能为空");
        }
    }

    private void validatePassword(UserEntity user, Errors errors) {
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            errors.rejectValue("password", "user.password.empty", "密码不能为空");
        }
    }

    private void validateEmail(UserEntity user, Errors errors) {
        String email = user.getEmail();
        if (email != null && !email.matches(EMAIL_PATTERN)) {
            errors.rejectValue("email", "user.email.invalid", "邮箱格式不正确");
        }
    }

    private void validatePhone(UserEntity user, Errors errors) {
        String phone = user.getCellphone();
        if (phone != null && !phone.matches(PHONE_PATTERN)) {
            errors.rejectValue("cellphone", "user.phone.invalid", "手机号格式不正确");
        }
    }
}
