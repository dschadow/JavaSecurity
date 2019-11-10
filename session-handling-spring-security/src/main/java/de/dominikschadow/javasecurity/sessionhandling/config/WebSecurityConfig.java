/*
 * Copyright (C) 2019 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.sessionhandling.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

/**
 * Spring Security configuration for the session handling sample project.
 *
 * @author Dominik Schadow
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private DataSource dataSource;

    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
        auth
            .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder());
        // @formatter:on
    }

    /**
     * BCryptPasswordEncoder takes a work factor as first argument. The default is 10, the valid range is 4 to 31. The
     * amount of work increases exponentially.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
                .antMatchers("/*", "/h2-console/**").permitAll()
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
            .and()
            .csrf()
                .ignoringAntMatchers("/h2-console/*")
            .and()
            .headers()
                .frameOptions().sameOrigin()
            .and()
            .formLogin()
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/");
        // @formatter:on
    }
}
