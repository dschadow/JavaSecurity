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
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the CSRFTokenHandler class.
 *
 * @author Dominik Schadow
 */
class CSRFTokenHandlerTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getToken_withNullSession_throwsServletException() {
        assertThrows(ServletException.class, () -> CSRFTokenHandler.getToken(null));
    }

    @Test
    void getToken_withValidSessionWithoutToken_generatesNewToken() throws Exception {
        when(session.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(null);

        String token = CSRFTokenHandler.getToken(session);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        verify(session).setAttribute(eq(CSRFTokenHandler.CSRF_TOKEN), anyString());
    }

    @Test
    void getToken_withValidSessionWithEmptyToken_generatesNewToken() throws Exception {
        when(session.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn("");

        String token = CSRFTokenHandler.getToken(session);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        verify(session).setAttribute(eq(CSRFTokenHandler.CSRF_TOKEN), anyString());
    }

    @Test
    void getToken_withValidSessionWithExistingToken_returnsExistingToken() throws Exception {
        String existingToken = "existingToken123";
        when(session.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(existingToken);

        String token = CSRFTokenHandler.getToken(session);

        assertEquals(existingToken, token);
        verify(session, never()).setAttribute(anyString(), anyString());
    }

    @Test
    void isValid_withNullSession_throwsServletException() {
        when(request.getSession(false)).thenReturn(null);

        assertThrows(ServletException.class, () -> CSRFTokenHandler.isValid(request));
    }

    @Test
    void isValid_withMatchingToken_returnsTrue() throws Exception {
        String csrfToken = "validToken123";
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(csrfToken);
        when(request.getParameter(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(csrfToken);

        boolean result = CSRFTokenHandler.isValid(request);

        assertTrue(result);
    }

    @Test
    void isValid_withNonMatchingToken_returnsFalse() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn("sessionToken");
        when(request.getParameter(CSRFTokenHandler.CSRF_TOKEN)).thenReturn("differentToken");

        boolean result = CSRFTokenHandler.isValid(request);

        assertFalse(result);
    }

    @Test
    void isValid_withNullRequestToken_returnsFalse() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn("sessionToken");
        when(request.getParameter(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(null);

        boolean result = CSRFTokenHandler.isValid(request);

        assertFalse(result);
    }

    @Test
    void isValid_withNullSessionToken_returnsFalse() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(null);
        when(request.getParameter(CSRFTokenHandler.CSRF_TOKEN)).thenReturn("requestToken");

        boolean result = CSRFTokenHandler.isValid(request);

        assertFalse(result);
    }

    @Test
    void isValid_withBothTokensNull_returnsTrue() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(null);
        when(request.getParameter(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(null);

        boolean result = CSRFTokenHandler.isValid(request);

        // When session has no token, getToken() generates a new one
        // So the tokens won't match
        assertFalse(result);
    }

    @Test
    void getToken_generatesUniqueTokens() throws Exception {
        HttpSession session1 = mock(HttpSession.class);
        HttpSession session2 = mock(HttpSession.class);
        when(session1.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(null);
        when(session2.getAttribute(CSRFTokenHandler.CSRF_TOKEN)).thenReturn(null);

        String token1 = CSRFTokenHandler.getToken(session1);
        String token2 = CSRFTokenHandler.getToken(session2);

        assertNotNull(token1);
        assertNotNull(token2);
        // Tokens should be different (with very high probability)
        assertNotEquals(token1, token2);
    }
}
