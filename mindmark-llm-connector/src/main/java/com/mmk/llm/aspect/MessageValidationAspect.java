package com.mmk.llm.aspect;

import com.mmk.llm.annotation.ValidMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class MessageValidationAspect {
    private static final String EMPTY_MESSAGE_ERROR = "对话消息不能为空。";

    @Before("@annotation(validMessage)")
    public void validateMessage(JoinPoint joinPoint, ValidMessage validMessage) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();
        String paramName = validMessage.paramName();

        for (int i = 0; i < parameters.length; i++) {
            String msg = extractMessage(parameters[i], args[i], paramName);
            if (msg != null && msg.trim().isEmpty()) {
                throw new IllegalArgumentException(EMPTY_MESSAGE_ERROR);
            }
        }
    }

    private String extractMessage(Parameter parameter, Object arg, String paramName) {
        if (arg == null) return null;
        
        if (parameter.isAnnotationPresent(RequestParam.class) && arg instanceof String) {
            return (String) arg;
        } else if (parameter.isAnnotationPresent(RequestBody.class)) {
            try {
                return (String) arg.getClass().getMethod("get" + capitalize(paramName)).invoke(arg);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
