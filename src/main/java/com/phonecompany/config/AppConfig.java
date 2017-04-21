package com.phonecompany.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages = { "com.phonecompany" })
public class AppConfig extends WebMvcConfigurerAdapter {
}
