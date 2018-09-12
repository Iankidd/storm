package org.storm.framework.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.storm.framework.base.resolver.SeacherConditionArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfg implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SeacherConditionArgumentResolver());
    }
}
