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
package de.dominikschadow.javasecurity.header.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

/**
 * Tests for the CSPReporting servlet class.
 *
 * @author Dominik Schadow
 */
class CSPReportingTest {
    private CSPReporting cspReporting;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cspReporting = new CSPReporting();
    }

    @Test
    void doPost_withValidCspReport_processesSuccessfully() throws Exception {
        String cspReport = """
            {
                "csp-report": {
                    "document-uri": "https://example.com/page.html",
                    "referrer": "",
                    "violated-directive": "script-src 'self'",
                    "effective-directive": "script-src",
                    "original-policy": "script-src 'self'; report-uri /csp/CSPReporting",
                    "blocked-uri": "https://evil.com/script.js",
                    "status-code": 200
                }
            }
            """;

        ServletInputStream servletInputStream = createServletInputStream(cspReport);
        when(request.getInputStream()).thenReturn(servletInputStream);

        cspReporting.doPost(request, response);

        verify(request).getInputStream();
    }

    @Test
    void doPost_withEmptyJsonObject_processesSuccessfully() throws Exception {
        String emptyJson = "{}";

        ServletInputStream servletInputStream = createServletInputStream(emptyJson);
        when(request.getInputStream()).thenReturn(servletInputStream);

        cspReporting.doPost(request, response);

        verify(request).getInputStream();
    }

    @Test
    void doPost_withInvalidJson_handlesJsonSyntaxException() throws Exception {
        String invalidJson = "{ invalid json }";

        ServletInputStream servletInputStream = createServletInputStream(invalidJson);
        when(request.getInputStream()).thenReturn(servletInputStream);

        cspReporting.doPost(request, response);

        verify(request).getInputStream();
    }

    @Test
    void doPost_withIOException_handlesException() throws Exception {
        when(request.getInputStream()).thenThrow(new IOException("Test IO Exception"));

        cspReporting.doPost(request, response);

        verify(request).getInputStream();
    }

    private ServletInputStream createServletInputStream(String content) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(javax.servlet.ReadListener readListener) {
            }
        };
    }
}
