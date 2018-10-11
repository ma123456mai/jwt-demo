package com.jwt.demo.config;

import com.jwt.demo.filter.JWTFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Mr丶s
 * @ClassName JWTConfig
 * @Version V1.0
 * @Date 2018/10/9 15:47
 * @Description
 */
@Configuration
public class JWTConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean jwtFilterRegistrationBean(){
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        JWTFilter jwtFilter = new JWTFilter();
        registrationBean.setFilter(jwtFilter);
        return registrationBean;
    }
}
