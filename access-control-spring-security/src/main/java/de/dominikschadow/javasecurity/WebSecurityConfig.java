/*
 * Copyright (C) 2022 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security configuration for the Access Control with Spring Security sample project.
 *
 * @author Dominik Schadow
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        // @formatter:off
        auth.
            inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser("userA")
                    .password(passwordEncoder.encode("userA"))
                    .authorities("ROLE_USER")
                .and()
                .withUser("userB")
                    .password(passwordEncoder.encode("userB"))
                    .authorities("ROLE_USER");
        // @formatter:on
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
                .antMatchers("/*", "/h2-console/**").permitAll()
                .antMatchers("/contacts/**").hasRole("USER")
             .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/*")
            .and()
            .headers()
                .frameOptions().sameOrigin()
            .and()
            .formLogin()
                .defaultSuccessUrl("/contacts")
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        // @formatter:on
    }
}
