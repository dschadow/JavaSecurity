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
package de.dominikschadow.javasecurity.csrf;

import de.dominikschadow.javasecurity.csrf.esapi.EsapiTokenHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Enterprise Security API secured order servlet for POST requests. Processes the order and returns the result
 *
 * @author Dominik Schadow
 */
@WebServlet(name = "ESAPIOrderServlet", urlPatterns = {"/ESAPIOrderServlet"})
public class ESAPIOrderServlet extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        logger.info("Processing ESAPI servlet...");

        try {
            if (!EsapiTokenHandler.isValid(request)) {
                logger.info("ESAPI servlet: CSRF token is invalid");
                response.setStatus(401);

                try (PrintWriter out = response.getWriter()) {
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" />");
                    out.println("<title>Cross-Site Request Forgery (CSRF): Invalid token</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>Cross-Site Request Forgery (CSRF): Invalid token</h1>");
                    out.println("<p><strong>Anti CSRF token is invalid!</strong></p>");
                    out.println("</body>");
                    out.println("</html>");
                } catch (IOException ex) {
                    logger.error(ex.getMessage(), ex);
                }

                return;
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            logger.error(ex.getMessage(), ex);
        }

        logger.info("ESAPI servlet: CSRF token is valid");

        String product = request.getParameter("product2");
        int quantity = Integer.parseInt(request.getParameter("quantity2"));

        logger.info("Ordered {} items of product {}", quantity, product);

        response.setContentType("text/html");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" />");
            out.println("<title>Cross-Site Request Forgery (CSRF): Order Confirmation</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Cross-Site Request Forgery (CSRF): Order Confirmation</h1>");
            out.println("<p><strong>Ordered " + quantity + " of product " + product + "</strong></p>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
