package com.tre.centralkitchen.common.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/userInfo").permitAll()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/master/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/settings/**").permitAll()
                .antMatchers("/subDepartments/**").permitAll()
                .antMatchers("/mtClient/**").permitAll()
                .antMatchers("/departments/**").permitAll()
                .antMatchers("/lines/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}
