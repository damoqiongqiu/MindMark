package com.mmk.rbac.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.google.code.kaptcha.impl.NoNoise;
import com.google.code.kaptcha.impl.ShadowGimpy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
import com.mmk.rbac.util.KaptchaTextCreator;

import static com.google.code.kaptcha.Constants.*;

/**
 * @author 大漠穷秋
 */
@Configuration
public class CaptchaConfig {
    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty(KAPTCHA_BORDER, "yes");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "38");
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCode");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, ShadowGimpy.class.getName());
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha getKaptchaBeanMath() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty(KAPTCHA_BORDER, "yes");
        properties.setProperty(KAPTCHA_BORDER_COLOR, "105,179,90");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "35");
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, KaptchaTextCreator.class.getName());
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        properties.setProperty(KAPTCHA_NOISE_COLOR, "white");
        properties.setProperty(KAPTCHA_NOISE_IMPL, NoNoise.class.getName());
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, ShadowGimpy.class.getName());
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
