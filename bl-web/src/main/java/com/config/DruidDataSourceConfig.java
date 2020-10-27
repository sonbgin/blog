package com.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songbin
 * @date 2020/10/8
 */
@Configuration
public class DruidDataSourceConfig {
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }

    //后台监控
    //springboot内置了servlet容器，没有web.xml 所以只能进行Bean配置
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>
                (new StatViewServlet(),"/druid/*");

        //后台需要有人登录
        HashMap<String,String> initParameter = new HashMap<>();
        //增加配置
        initParameter.put("loginUsername","admin");  //登录的key是固定的 loginUsername  loginPassword
        initParameter.put("loginPassword","123456");
        //访问权限
        //允许谁可以访问
        initParameter.put("allow","");

        bean.setInitParameters(initParameter);
        return bean;
    }

    //filter  过滤器配置
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());

        Map<String,String> initParameter = new HashMap<>();
        //可以过滤那些请求

        //以下内容不进行统计
        initParameter.put("exclusions","*.js,*.css,/druid/*");
        bean.setInitParameters(initParameter);
        return bean;
    }
}
