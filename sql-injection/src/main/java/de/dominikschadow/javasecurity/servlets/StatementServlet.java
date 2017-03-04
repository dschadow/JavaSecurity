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
package de.dominikschadow.javasecurity.servlets;

import de.dominikschadow.javasecurity.listener.ConnectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet using a normal Statement to query the in-memory-database. User input is not modified and used directly in the
 * SQL query. {@code ' or '1'='1} is a good input to return all statements, {@code '; drop table customer;--} to delete
 * the complete table.
 *
 * @author Dominik Schadow
 */
@WebServlet(name = "StatementServlet", urlPatterns = {"/StatementServlet"})
public class StatementServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(StatementServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String name = request.getParameter("name");
        log.info("Received " + name + " as POST parameter");

        String query = "SELECT * FROM customer WHERE name = '" + name + "' ORDER BY CUST_ID";

        log.info("Final SQL query " + query);

        response.setContentType("text/html");

        try (Statement stmt = ConnectionListener.con.createStatement(); ResultSet rs = stmt.executeQuery(query);
             PrintWriter out = response.getWriter()) {
            out.println("<html><head>");
            out.println("<title>SQL Injection - Statement</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"resources/css/styles.css\" />");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>SQL Injection - Statement</h1>");
            out.println("<p><strong>Input</strong> " + name + "</p>");
            out.println("<h2>Customer Data</h2>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Name</th>");
            out.println("<th>Status</th>");
            out.println("<th>Order Limit</th>");
            out.println("</tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt(1) + "</td>");
                out.println("<td>" + rs.getString(2) + "</td>");
                out.println("<td>" + rs.getString(3) + "</td>");
                out.println("<td>" + rs.getInt(4) + "</td>");
                out.println("</tr>");
            }

            out.println("<table>");
            out.println("<p><a href=\"index.jsp\">Home</a></p>");
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException | IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
