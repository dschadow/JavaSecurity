/*
 * Copyright (C) 2018 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Java Security project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dominikschadow.javasecurity.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security configuration for the Access Control with Spring Security sample project.
 *
 * @author Dominik Schadow
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * BCryptPasswordEncoder takes a work factor as first argument. The default is 10, the valid range is
     * 4 to 31. The amount of work increases exponentially.
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
        auth.
            inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder(10))
                .withUser("userA")
                    .password("$2a$10$DPvGhj5Y4vjVhSKx8nT1i.1LeALEk7.njHrql1g2k3kGm3l82bu8O")
                    .authorities("ROLE_USER")
                .and()
                .withUser("userB")
                    .password("$2a$10$XM1VDywhhoIqZfwC5f.3I.NW5.ahj5Yoo4au5jv4IStKmVK3LFxme")
                    .authorities("ROLE_USER");
        // @formatter:on
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
                .antMatchers("/*").permitAll()
                .antMatchers("/contacts/**").hasRole("USER")
            .and()
            .formLogin()
                .defaultSuccessUrl("/contacts")
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        // @formatter:on
    }
}
