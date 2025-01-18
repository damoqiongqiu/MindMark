package com.mmk.rbac.validation;

import jakarta.validation.Payload;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.*;

@Documented
@Validated
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUser {
    String message() default "用户数据验证失败";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
