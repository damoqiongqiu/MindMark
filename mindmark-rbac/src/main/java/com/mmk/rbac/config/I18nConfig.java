package com.mmk.rbac.config;

import com.mmk.core.i18n.I18nService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Spring 默认实现了 I18n，这里在配置类里简单配置一下。当然您可以在 application 配置文件里进行配置。
 * 切换国际化需要在请求头添加 Accept-Language，举例 Accept-Language=zh
 */
@Configuration
public class I18nConfig {

    /**
     * RESOURCE_BUNDLE 资源包名称
     * 包下资源文件，您可以自行添加，注意格式
     * 举例:
     * messages_en.properties
     * messages.properties
     */
    private static final String RESOURCE_BUNDLE = "i18n/messages";

    @Bean
    public I18nService i18nService() {
        return new I18nService(messageSource());
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames(RESOURCE_BUNDLE);
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return source;
    }
}
