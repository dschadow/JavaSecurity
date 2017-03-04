/*
 * Copyright (C) 2017 Dominik Schadow, dominikschadow@gmail.com
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
 * This servlet filter protects the {@code csp/protected.jsp} page by adding the {@code Content-Security-Policy}
 * header to the response. The {@code urlPatterns} should be far more wildcard in a real web application than in this
 * demo project.
 *
 * @author Dominik Schadow
 */
@WebFilter(filterName = "CSPFilter", urlPatterns = {"/csp/protected.jsp"})
public class CSPFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(CSPFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        log.info("Content-Security-Policy header added to response");

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Content-Security-Policy", "default-src 'self'; report-uri CSPReporting");

        filterChain.doFilter(servletRequest, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
