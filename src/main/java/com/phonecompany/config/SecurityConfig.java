package com.phonecompany.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private ShaPasswordEncoder shaPasswordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          ShaPasswordEncoder shaPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.shaPasswordEncoder = shaPasswordEncoder;
    }

    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(shaPasswordEncoder);
    }

    @Autowired
    private RESTAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private RESTAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private RESTAuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/view/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/api/users").hasRole("ADMIN")
                .antMatchers("/api/customers").permitAll()
                .antMatchers("/api/user/reset").permitAll()
                .antMatchers("/api/confirmRegistration").permitAll()
                .antMatchers("/api/roles").hasRole("ADMIN")
                .antMatchers("/api/regions/get").hasAnyRole("CSR","ADMIN")
                .antMatchers("/api/tariffs/get/by/region/**").hasAnyRole("CSR","ADMIN")
                .antMatchers("/api/tariff/new/get").hasAnyRole("CSR","ADMIN")
                .antMatchers("/api/tariff/add").hasAnyRole("CSR","ADMIN")
                .antMatchers("/api/tariff/add/single").hasAnyRole("CSR","ADMIN")
                .antMatchers("/api/tariff/update").hasAnyRole("CSR","ADMIN")
                .antMatchers("/api/tariff/update/single").hasAnyRole("CSR","ADMIN")
                .antMatchers("/api/tariff/get/*").hasAnyRole("CSR","ADMIN")
                .antMatchers("/api/tariff/update/status/**").hasAnyRole("CSR", "ADMIN")
                .anyRequest().authenticated();

        http.csrf().disable();
        http
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().logoutSuccessUrl("/#/index").permitAll();
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.formLogin().successHandler(authenticationSuccessHandler);
        http.formLogin().failureHandler(authenticationFailureHandler);
    }
}
