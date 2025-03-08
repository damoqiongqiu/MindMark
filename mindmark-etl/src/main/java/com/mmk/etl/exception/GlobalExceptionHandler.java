package com.mmk.etl.exception;

import com.mmk.rbac.i18n.I18nUtil;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Locale;

/**
 *  TODO: 统一异常在这里写，暂时先实现 上传文件大小超过限制时的异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException e, Locale locale) {
        String message = I18nUtil.getMessage(
                "upload.size.exceeded",
                new Object[]{maxFileSize}
        );
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<String> handleUnauthenticatedException(UnauthenticatedException e) {
        String message = I18nUtil.getMessage("user.not.authenticated");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }
}