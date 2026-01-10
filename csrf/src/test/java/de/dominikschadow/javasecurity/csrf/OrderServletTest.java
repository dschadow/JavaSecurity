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
package de.dominikschadow.javasecurity.csrf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the OrderServlet class.
 *
 * @author Dominik Schadow
 */
class OrderServletTest {
    private OrderServlet orderServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        orderServlet = new OrderServlet();
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void doPost_withValidToken_returnsOrderConfirmation() throws Exception {
        String csrfToken = "validToken123";

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(csrfToken);
        when(request.getParameter(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(csrfToken);
        when(request.getParameter("product")).thenReturn("TestProduct");
        when(request.getParameter("quantity")).thenReturn("5");

        orderServlet.doPost(request, response);

        printWriter.flush();
        String output = stringWriter.toString();

        verify(response).setContentType("text/html");
        assertTrue(output.contains("Order Confirmation"));
        assertTrue(output.contains("Ordered 5 of product TestProduct"));
    }

    @Test
    void doPost_withInvalidToken_returns401() throws Exception {
        String sessionToken = "sessionToken123";
        String requestToken = "differentToken456";

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(sessionToken);
        when(request.getParameter(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(requestToken);

        orderServlet.doPost(request, response);

        printWriter.flush();
        String output = stringWriter.toString();

        verify(response).setStatus(401);
        assertTrue(output.contains("Invalid token"));
        assertTrue(output.contains("Anti CSRF token is invalid!"));
    }

    @Test
    void doPost_withMissingToken_returns401() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn("sessionToken");
        when(request.getParameter(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(null);

        orderServlet.doPost(request, response);

        printWriter.flush();
        String output = stringWriter.toString();

        verify(response).setStatus(401);
        assertTrue(output.contains("Invalid token"));
    }

    @Test
    void doPost_withInvalidQuantity_setsQuantityToZero() throws Exception {
        String csrfToken = "validToken123";

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(csrfToken);
        when(request.getParameter(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(csrfToken);
        when(request.getParameter("product")).thenReturn("TestProduct");
        when(request.getParameter("quantity")).thenReturn("invalid");

        orderServlet.doPost(request, response);

        printWriter.flush();
        String output = stringWriter.toString();

        assertTrue(output.contains("Ordered 0 of product TestProduct"));
    }

    @Test
    void doPost_withNoSession_throwsServletException() {
        when(request.getSession(false)).thenReturn(null);

        assertThrows(ServletException.class, () -> orderServlet.doPost(request, response));
    }
}
