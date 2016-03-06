/*
 * Copyright (C) 2016 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.header.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet filter protects the {@code csp2/protected.jsp} page by adding the {@code Content-Security-Policy}
 * Level 2 header to the response. The {@code urlPatterns} should be far more wildcard in a real web application than
 * in this demo project.
 *
 * @author Dominik Schadow
 */
@WebFilter(filterName = "CSP2Filter", urlPatterns = {"/csp2/protectedForm.jsp", "/all/all.jsp"})
public class CSP2Filter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSP2Filter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("Content-Security-Policy Level 2 header added to response");

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Content-Security-Policy", "default-src 'self'; frame-ancestors 'none'; reflected-xss block");

        filterChain.doFilter(servletRequest, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
