package com.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author songbin
 * @date 2020/10/9
 */
@Configuration
public class ShiroSecurityConfig {

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(
            @Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        Map<String, String> filterChainMap = new LinkedHashMap<>();
        
        //释放静态资源
        filterChainMap.put("/css/**","anon");
        filterChainMap.put("/js/**","anon");
        filterChainMap.put("/img/**","anon");
        filterChainMap.put("/favicon.ico","anon");
        filterChainMap.put("/lib","anon");
        filterChainMap.put("/admin","anon");
        //释放游客浏览页面
        filterChainMap.put("/ordinary/**","anon");
        //登录授权
        filterChainMap.put("/**","authc[user:admin]");

        bean.setFilterChainDefinitionMap(filterChainMap);

        //设置登录请求
        bean.setLoginUrl("/admin");

        //设置未授权的页面
        bean.setUnauthorizedUrl("/noauth");
        return bean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(
            @Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }

    @Bean
    //整合shiroDialect：用来整合shiro thymeleaf
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
}
