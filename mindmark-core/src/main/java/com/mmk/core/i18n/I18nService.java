package com.mmk.core.i18n;

import org.springframework.context.MessageSource;

public class I18nService {

    private final MessageSource messageSource;

    public I18nService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocalizationUtils.getCurrentLocale());
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocalizationUtils.getCurrentLocale());
    }
}
