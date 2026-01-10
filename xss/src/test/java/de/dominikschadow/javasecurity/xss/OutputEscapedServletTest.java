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
 * Tests for the OutputEscapedServlet class.
 *
 * @author Dominik Schadow
 */
class OutputEscapedServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private OutputEscapedServlet servlet;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new OutputEscapedServlet();
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    void doPost_setsContentTypeToHtml() throws Exception {
        when(request.getParameter("outputEscapedName")).thenReturn("TestName");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);

        verify(response).setContentType("text/html");
    }

    @Test
    void doPost_outputContainsName() throws Exception {
        String testName = "TestUser";
        when(request.getParameter("outputEscapedName")).thenReturn(testName);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains(testName));
    }

    @Test
    void doPost_outputContainsHtmlStructure() throws Exception {
        when(request.getParameter("outputEscapedName")).thenReturn("TestName");
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
        when(request.getParameter("outputEscapedName")).thenReturn("TestName");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("<title>Cross-Site Scripting (XSS) - Output Escaping</title>"));
    }

    @Test
    void doPost_outputContainsHomeLink() throws Exception {
        when(request.getParameter("outputEscapedName")).thenReturn("TestName");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("index.jsp"));
        assertTrue(output.contains("Home"));
    }

    @Test
    void doPost_outputContainsStylesheetLink() throws Exception {
        when(request.getParameter("outputEscapedName")).thenReturn("TestName");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("resources/css/styles.css"));
    }

    @Test
    void doPost_outputContainsHeading() throws Exception {
        when(request.getParameter("outputEscapedName")).thenReturn("TestName");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("<h1>Cross-Site Scripting (XSS) - Output Escaping</h1>"));
    }

    @Test
    void doPost_withNullName_handlesGracefully() throws Exception {
        when(request.getParameter("outputEscapedName")).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("<html>"));
        assertTrue(output.contains("</html>"));
    }

    @Test
    void doPost_withEmptyName_handlesGracefully() throws Exception {
        when(request.getParameter("outputEscapedName")).thenReturn("");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("<html>"));
        assertTrue(output.contains("</html>"));
    }

    @Test
    void doPost_withScriptTag_escapesOutput() throws Exception {
        String maliciousInput = "<script>alert('XSS')</script>";
        when(request.getParameter("outputEscapedName")).thenReturn(maliciousInput);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        // The output should NOT contain the raw script tag due to escaping
        assertFalse(output.contains("<script>alert('XSS')</script>"));
        // The output should contain the escaped version
        assertTrue(output.contains("&lt;script&gt;"));
    }

    @Test
    void doPost_withSpecialCharacters_escapesOutput() throws Exception {
        String specialChars = "Test<>&\"'Name";
        when(request.getParameter("outputEscapedName")).thenReturn(specialChars);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        // The output should NOT contain raw special characters in the escaped sections
        // Check that < and > are escaped in the body content
        assertTrue(output.contains("&lt;") || output.contains("&gt;") || output.contains("&amp;"));
    }

    @Test
    void doPost_outputContainsHelloGreeting() throws Exception {
        when(request.getParameter("outputEscapedName")).thenReturn("TestName");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("<strong>Hello </strong>"));
    }

    @Test
    void doPost_outputContainsTitleAttribute() throws Exception {
        String testName = "TestUser";
        when(request.getParameter("outputEscapedName")).thenReturn(testName);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("title='Hello " + testName + "'"));
    }

    @Test
    void doPost_withHtmlInName_escapesHtmlAttribute() throws Exception {
        String maliciousInput = "' onclick='alert(1)'";
        when(request.getParameter("outputEscapedName")).thenReturn(maliciousInput);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        // The attribute should be escaped, so the raw onclick should not appear
        assertFalse(output.contains("onclick='alert(1)'"));
        // The escaped version should contain encoded characters
        assertTrue(output.contains("&#39;") || output.contains("&apos;"));
    }
}
