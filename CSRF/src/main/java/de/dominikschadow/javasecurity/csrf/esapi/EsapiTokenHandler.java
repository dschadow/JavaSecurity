/*
 * Copyright (C) 2013 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Java Security Myths project.
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
package de.dominikschadow.javasecurity.csrf.esapi;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

/**
 * @author Dominik Schadow
 */
public final class EsapiTokenHandler {
    public static final String CSRF_TOKEN = "CSRF_TOKEN";
    private static final String MISSING_SESSION = "No session available";

    private static String getToken() throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        sr.nextBytes(new byte[20]);
        return String.valueOf(sr.nextLong());
    }

    public static String getToken(HttpSession session) throws ServletException, NoSuchAlgorithmException,
            NoSuchProviderException {
        if (session == null) {
            throw new ServletException(MISSING_SESSION);
        }

        String token = (String) session.getAttribute(CSRF_TOKEN);

        if (StringUtils.isEmpty(token)) {
            token = getToken();
            session.setAttribute(CSRF_TOKEN, token);
        }

        return token;
    }

    public static boolean isValid(HttpServletRequest request) throws ServletException, NoSuchAlgorithmException,
            NoSuchProviderException {
        if (request.getSession(false) == null) {
            throw new ServletException(MISSING_SESSION);
        }

        return StringUtils.equals(getToken(request.getSession(false)), request.getParameter(CSRF_TOKEN));
    }
}