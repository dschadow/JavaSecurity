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
package de.dominikschadow.javasecurity.header.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet filter protects the {@code x-content-type-options/protected.txt} against content sniffing attacks by
 * adding the {@code X-Content-Type-Options} header and the content type to the response. The {@code urlPatterns} should
 * be far more wildcard in a real web application than in this demo project, and the content type would be provided
 * individually, e.g. by a servlet.
 *
 * @author Dominik Schadow
 */
@WebFilter(filterName = "XContentTypeOptionsFilter", urlPatterns = {"/x-content-type-options/protected.txt",
        "/all/all.jsp"})
public class XContentTypeOptionsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setContentType("text/plain");
        response.addHeader("X-Content-Type-Options", "nosniff");

        filterChain.doFilter(servletRequest, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
