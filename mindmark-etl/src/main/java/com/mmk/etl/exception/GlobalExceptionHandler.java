package com.mmk.etl.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.http.ResponseEntity;

/**
 *  TODO: 统一异常在这里写，暂时先实现 上传文件大小超过限制时的异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException e) {
        // TODO 提醒文本国际化
        return ResponseEntity.badRequest().body("The file size must not be larger than 5MB.");
    }
}