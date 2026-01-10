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
package de.dominikschadow.javasecurity.sessionhandling.servlets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the LoginServlet class.
 *
 * @author Dominik Schadow
 */
class LoginServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    private LoginServlet servlet;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new LoginServlet();
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    void doPost_changesSessionId() throws Exception {
        String originalSessionId = "originalSession123";
        String newSessionId = "newSession456";

        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(originalSessionId);
        when(request.changeSessionId()).thenReturn(newSessionId);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);

        verify(request).getSession();
        verify(request).changeSessionId();
    }

    @Test
    void doPost_setsContentTypeToHtml() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("sessionId");
        when(request.changeSessionId()).thenReturn("newSessionId");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);

        verify(response).setContentType("text/html");
    }

    @Test
    void doPost_setsCharacterEncodingToUTF8() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("sessionId");
        when(request.changeSessionId()).thenReturn("newSessionId");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);

        verify(response).setCharacterEncoding("UTF-8");
    }

    @Test
    void doPost_outputContainsOriginalSessionId() throws Exception {
        String originalSessionId = "originalSession123";
        String newSessionId = "newSession456";

        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(originalSessionId);
        when(request.changeSessionId()).thenReturn(newSessionId);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains(originalSessionId));
    }

    @Test
    void doPost_outputContainsNewSessionId() throws Exception {
        String originalSessionId = "originalSession123";
        String newSessionId = "newSession456";

        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(originalSessionId);
        when(request.changeSessionId()).thenReturn(newSessionId);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains(newSessionId));
    }

    @Test
    void doPost_outputContainsHtmlStructure() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("sessionId");
        when(request.changeSessionId()).thenReturn("newSessionId");
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
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("sessionId");
        when(request.changeSessionId()).thenReturn("newSessionId");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("<title>Session Handling</title>"));
    }

    @Test
    void doPost_outputContainsHomeLink() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("sessionId");
        when(request.changeSessionId()).thenReturn("newSessionId");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("index.jsp"));
        assertTrue(output.contains("Home"));
    }

    @Test
    void doPost_outputContainsStylesheetLink() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("sessionId");
        when(request.changeSessionId()).thenReturn("newSessionId");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("resources/css/styles.css"));
    }

    @Test
    void doPost_sessionIdsDifferInOutput() throws Exception {
        String originalSessionId = "original123";
        String newSessionId = "new456";

        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(originalSessionId);
        when(request.changeSessionId()).thenReturn(newSessionId);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doPost(request, response);
        printWriter.flush();

        String output = stringWriter.toString();
        assertTrue(output.contains("Original Session ID"));
        assertTrue(output.contains("New Session ID"));
        assertNotEquals(originalSessionId, newSessionId);
    }
}
