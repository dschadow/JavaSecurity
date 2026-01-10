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
package de.dominikschadow.javasecurity.xss;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the UnprotectedServlet class.
 *
 * @author Dominik Schadow
 */
class UnprotectedServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private UnprotectedServlet servlet;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new UnprotectedServlet();
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    void doPost_setsContentTypeToHtml() throws Exception {
        when(request.getParameter("unprotectedName")).thenReturn("TestName");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);

        verify(response).setContentType("text/html");
    }

    @Test
    void doPost_outputContainsName() throws Exception {
        String testName = "TestUser";
        when(request.getParameter("unprotectedName")).thenReturn(testName);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("[" + testName + "]"));
    }

    @Test
    void doPost_outputContainsHtmlStructure() throws Exception {
        when(request.getParameter("unprotectedName")).thenReturn("TestName");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("<html>"));
        assertTrue(output.contains("</html>"));
        assertTrue(output.contains("<head>"));
        assertTrue(output.contains("</head>"));
        assertTrue(output.contains("<body>"));
        assertTrue(output.contains("</body>"));
    }

    @Test
    void doPost_outputContainsTitle() throws Exception {
        when(request.getParameter("unprotectedName")).thenReturn("TestName");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("<title>Cross-Site Scripting (XSS) - Unprotected</title>"));
    }

    @Test
    void doPost_outputContainsHomeLink() throws Exception {
        when(request.getParameter("unprotectedName")).thenReturn("TestName");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("index.jsp"));
        assertTrue(output.contains("Home"));
    }

    @Test
    void doPost_outputContainsStylesheetLink() throws Exception {
        when(request.getParameter("unprotectedName")).thenReturn("TestName");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("resources/css/styles.css"));
    }

    @Test
    void doPost_outputContainsHeading() throws Exception {
        when(request.getParameter("unprotectedName")).thenReturn("TestName");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("<h1>Cross-Site Scripting (XSS) - Unprotected</h1>"));
    }

    @Test
    void doPost_withNullName_outputContainsNull() throws Exception {
        when(request.getParameter("unprotectedName")).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("[null]"));
    }

    @Test
    void doPost_withEmptyName_outputContainsEmptyBrackets() throws Exception {
        when(request.getParameter("unprotectedName")).thenReturn("");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("[]"));
    }

    @Test
    void doPost_withScriptTag_outputContainsScriptTagUnescaped() throws Exception {
        String maliciousInput = "<script>alert('XSS')</script>";
        when(request.getParameter("unprotectedName")).thenReturn(maliciousInput);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        // UnprotectedServlet does NOT escape the input, demonstrating XSS vulnerability
        assertTrue(output.contains("[" + maliciousInput + "]"));
    }

    @Test
    void doPost_withSpecialCharacters_outputContainsSpecialCharactersUnescaped() throws Exception {
        String specialChars = "Test<>&\"'Name";
        when(request.getParameter("unprotectedName")).thenReturn(specialChars);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        // UnprotectedServlet does NOT escape special characters
        assertTrue(output.contains("[" + specialChars + "]"));
    }

    @Test
    void doPost_withHtmlInjection_outputContainsHtmlUnescaped() throws Exception {
        String htmlInjection = "<img src='x' onerror='alert(1)'>";
        when(request.getParameter("unprotectedName")).thenReturn(htmlInjection);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        // UnprotectedServlet does NOT escape HTML, demonstrating vulnerability
        assertTrue(output.contains("[" + htmlInjection + "]"));
    }
}
