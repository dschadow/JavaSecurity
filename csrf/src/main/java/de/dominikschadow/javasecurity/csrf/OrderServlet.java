/*
 * Copyright (C) 2017 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.csrf;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;

/**
 * CSRF secured order servlet for POST requests. Processes the order and returns the result.
 *
 * @author Dominik Schadow
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/OrderServlet"})
public class OrderServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 168055850789919449L;
    private static final System.Logger LOG = System.getLogger(OrderServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        LOG.log(System.Logger.Level.INFO, "Processing order servlet...");

        if (!CSRFTokenHandler.isValid(request)) {
            LOG.log(System.Logger.Level.INFO, "Order servlet: CSRF token is invalid");
            response.setStatus(401);

            try (PrintWriter out = response.getWriter()) {
                out.println("<html>");
                out.println("<head>");
                out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
                out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"resources/css/styles.css\" />");
                out.println("<title>Cross-Site Request Forgery (CSRF): Invalid token</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Cross-Site Request Forgery (CSRF): Invalid token</h1>");
                out.println("<p><strong>Anti CSRF token is invalid!</strong></p>");
                out.println("<p><a href=\"index.jsp\">Home</a></p>");
                out.println("</body>");
                out.println("</html>");
            } catch (IOException ex) {
                LOG.log(System.Logger.Level.ERROR, ex.getMessage(), ex);
            }

            return;
        }

        LOG.log(System.Logger.Level.INFO, "Order servlet: CSRF token is valid");

        String product = request.getParameter("product");
        int quantity;

        try {
            quantity = Integer.parseInt(request.getParameter("quantity"));
        } catch (NumberFormatException ex) {
            quantity = 0;
        }

        LOG.log(System.Logger.Level.INFO, "Ordered {0} items of product {1}", quantity, product);

        response.setContentType("text/html");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"resources/css/styles.css\" />");
            out.println("<title>Cross-Site Request Forgery (CSRF): Order Confirmation</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Cross-Site Request Forgery (CSRF): Order Confirmation</h1>");
            out.println("<p><strong>Ordered " + quantity + " of product " + product + "</strong></p>");
            out.println("<p><a href=\"index.jsp\">Home</a></p>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            LOG.log(System.Logger.Level.ERROR, ex.getMessage(), ex);
        }
    }
}
