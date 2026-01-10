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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Tests for the LoginServlet class.
 *
 * @author Dominik Schadow
 */
class LoginServletTest {
    private LoginServlet loginServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginServlet = new LoginServlet();
    }

    @Test
    void doPost_returnsHtmlWithSuccessMessage() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        loginServlet.doPost(request, response);

        verify(response).setContentType("text/html; charset=UTF-8");
        verify(response).getWriter();

        String htmlOutput = stringWriter.toString();
        assertTrue(htmlOutput.contains("<html>"));
        assertTrue(htmlOutput.contains("</html>"));
        assertTrue(htmlOutput.contains("<title>Security Response Header</title>"));
        assertTrue(htmlOutput.contains("<h1>Login successful</h1>"));
        assertTrue(htmlOutput.contains("<a href=\"../index.jsp\">Home</a>"));
    }

    @Test
    void doPost_setsCorrectContentType() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        loginServlet.doPost(request, response);

        verify(response).setContentType("text/html; charset=UTF-8");
    }

    @Test
    void doPost_containsStylesheetLink() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        loginServlet.doPost(request, response);

        String htmlOutput = stringWriter.toString();
        assertTrue(htmlOutput.contains("../resources/css/styles.css"));
    }
}
