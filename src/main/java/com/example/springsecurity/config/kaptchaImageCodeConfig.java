package com.example.springsecurity.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class kaptchaImageCodeConfig {

    @Bean
    public DefaultKaptcha getDefaultKaptcha(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty(Constants.KAPTCHA_BORDER,"yes");
        properties.setProperty(Constants.KAPTCHA_BORDER_COLOR,"192,192,192");
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH,"120");
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT,"36");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR,"blue");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE,"28");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES,"宋体");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH,"4");
        properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL,"com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;

    }
}
