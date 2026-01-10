/*
 * Copyright (C) 2026 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Java Security project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dominikschadow.javasecurity.header.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

/**
 * Tests for the XFrameOptionsFilter class.
 *
 * @author Dominik Schadow
 */
class XFrameOptionsFilterTest {
    private XFrameOptionsFilter xFrameOptionsFilter;

    @Mock
    private ServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private FilterConfig filterConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        xFrameOptionsFilter = new XFrameOptionsFilter();
    }

    @Test
    void doFilter_setsXFrameOptionsHeader() throws Exception {
        xFrameOptionsFilter.doFilter(request, response, filterChain);

        verify(response).addHeader("X-Frame-Options", "DENY");
    }

    @Test
    void doFilter_callsFilterChain() throws Exception {
        xFrameOptionsFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilter_setsHeaderAndContinuesChain() throws Exception {
        xFrameOptionsFilter.doFilter(request, response, filterChain);

        verify(response).addHeader("X-Frame-Options", "DENY");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void init_doesNotThrowException() {
        xFrameOptionsFilter.init(filterConfig);

        verifyNoInteractions(filterConfig);
    }

    @Test
    void destroy_doesNotThrowException() {
        xFrameOptionsFilter.destroy();
    }
}
