/*
 * Copyright (C) 2018 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.logging.servlets;

import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Simple login servlet which returns a success message and logs security relevant events into the log file.
 *
 * @author Dominik Schadow
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = -660893987741671511L;
    private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        log.info(SecurityMarkers.CONFIDENTIAL, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.EVENT_FAILURE, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.EVENT_SUCCESS, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.RESTRICTED, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.SECRET, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.SECURITY_AUDIT, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.SECURITY_FAILURE, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.SECURITY_SUCCESS, "User {} with password {} logged in", username, password);
        log.info(SecurityMarkers.TOP_SECRET, "User {} with password {} logged in", username, password);

        response.setContentType("text/html; charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../resources/css/styles.css\" />");
            out.println("<title>Security Logging</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Login successful</h1>");
            out.println("<div><a href=\"/security-logging\">Home</a></div>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
