package com.phonecompany.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


import javax.servlet.Filter;

@Configuration
@ComponentScan(basePackages = { "com.phonecompany" })
public class AppConfig extends WebMvcConfigurerAdapter {
}
