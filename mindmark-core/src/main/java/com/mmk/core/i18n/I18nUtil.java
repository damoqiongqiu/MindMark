package com.mmk.core.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * 在静态工具类Util封装一下 i18nService，省去在要使用I18n对应方法时，每次都要在类中声明 I18nService 类型的 成员变量
 */
@Component
public class I18nUtil {

    @Autowired
    private I18nService i18nService;

    private static I18nUtil i18nUtil;

    @PostConstruct
    public void init() {
        i18nUtil = this;
        i18nUtil.i18nService = this.i18nService;
    }

    public static String getMessage(String code, Object[] args) {
        return i18nUtil.i18nService.getMessage(code, args);
    }
    public static String getMessage(String code) {
       return i18nUtil.i18nService.getMessage(code);
    }

}
