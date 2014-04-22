/*
 * Copyright (C) 2014 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of Java-Web-Security
.
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
package de.dominikschadow.javasecurity.xss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet which sets the <code>Content-Security-Policy-Report-Only</code> response header and reports
 * any JavaScript code that would have been stopped by the policy.
 *
 * @author Dominik Schadow
 */
@WebServlet(name = "CSPReportingServlet", urlPatterns = {"/CSPReportingServlet"})
public class CSPReportingServlet extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        logger.info("Processing POST request with Content Security Policy Reporty Only");

        String name = request.getParameter("name3");
        logger.info("Received " + name + " as POST parameter");

        response.setContentType("text/html");
        // the following line only reports violations and does not block anything
        response.setHeader("Content-Security-Policy-Report-Only", "default-src 'self'; report-uri CSPReporting");

        // use the following line to activate the policy and still report all violations
//        response.setHeader("Content-Security-Policy", "default-src 'self'; report-uri CSPReporting");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" />");
            out.println("<title>Cross-Site Scripting (XSS): Content Security Policy Reporty Only</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Cross-Site Scripting (XSS): Content Security Policy Reporty Only</h1>");
            out.println("<p><strong>Hello</strong> " + name + "</p>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
