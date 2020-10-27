package com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author songbin
 * @date 2020/9/29
 */
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    //添加视图处理
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }

}
