package com.mmk.rbac.i18n;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 *
 * 返回与当前线程关联的区域设置（如果有），否则返回系统默认区域设置
 * @author nanke
 */
public class LocalizationUtils {

    private LocalizationUtils() {
    }

    public static Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }
}
