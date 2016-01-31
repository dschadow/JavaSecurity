/*
 * Copyright (C) 2016 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.sessionhandling.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServlet.class);
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String currentSessionId = request.getSession().getId();

        LOGGER.info("Original session ID {}", currentSessionId);

        // changes the session id in the session, returns the new one
        String newSessionId = request.changeSessionId();

        LOGGER.info("New session ID {}", newSessionId);

        response.setContentType("text/html");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head>");
            out.println("<title>Session Handling</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"resources/css/styles.css\" />");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Session Handling</h1>");
            out.println("<p><strong>Original Session ID: </strong> " + currentSessionId + "</p>");
            out.println("<p><strong>New Session ID: </strong> " + newSessionId + "</p>");
            out.println("<p><a href=\"index.jsp\">Home</a></p>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
