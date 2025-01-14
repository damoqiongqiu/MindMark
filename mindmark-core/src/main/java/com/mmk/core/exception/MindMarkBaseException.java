package com.mmk.core.exception;

/**
 * MindMark 的基础异常类，所有业务异常都应该继承此类。
 * MindMark 的日志分析工具可以针对此异常类的格式进行日志分析和审计。
 * @author 大漠穷秋
 */
public class MindMarkBaseException extends RuntimeException {
    //异常所属的业务模块
    private String module;

    //异常代码
    private String code;

    //异常消息
    private String message;

    public MindMarkBaseException() {
        super();
    }

    public MindMarkBaseException(Throwable cause) {
        super(cause);
    }

    public MindMarkBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public MindMarkBaseException(String module, String code, String message) {
        this.module = module;
        this.code = code;
        this.message = message;
    }

    public MindMarkBaseException(String message) {
        this(null, null, message);
    }

    public MindMarkBaseException(String module, String code) {
        this(module, code, null);
    }

    public String getModule() {
        return module;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
