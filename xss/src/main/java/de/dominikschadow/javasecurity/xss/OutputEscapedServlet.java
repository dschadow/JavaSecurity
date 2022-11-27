/*
 * Copyright (C) 2022 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.xss;

import org.owasp.encoder.Encode;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;

/**
 * Servlet to return output escaping user input to prevent Cross-Site Scripting (XSS).
 *
 * @author Dominik Schadow
 */
@WebServlet(name = "OutputEscapedServlet", urlPatterns = {"/escaped"})
public class OutputEscapedServlet extends HttpServlet {
	@Serial
    private static final long serialVersionUID = 2290746121319783879L;
    private static final System.Logger LOG = System.getLogger(OutputEscapedServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("outputEscapedName");

        LOG.log(System.Logger.Level.INFO, "Received {0} as name", name);

        response.setContentType("text/html");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head>");
            out.println("<title>Cross-Site Scripting (XSS) - Output Escaping</title>");
            out.println("<link rel='stylesheet' type='text/css' href='resources/css/styles.css' />");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Cross-Site Scripting (XSS) - Output Escaping</h1>");
            out.println("<p title='Hello " + Encode.forHtmlAttribute(name) + "'><strong>Hello </strong>");
            Encode.forHtml(out, name);
            out.println("</p>");
            out.println("<p><a href='index.jsp'>Home</a></p>");
            out.println("</body></html>");
        } catch (IOException ex) {
            LOG.log(System.Logger.Level.ERROR, ex.getMessage(), ex);
        }
    }
}
