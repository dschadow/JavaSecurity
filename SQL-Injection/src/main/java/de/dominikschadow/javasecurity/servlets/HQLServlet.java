/*
 * Copyright (C) 2015 Dominik Schadow, dominikschadow@gmail.com
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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dominikschadow.javasecurity.domain.Customer;

/**
 * Servlet using Hibernate Query Language (HQL) to query the in-memory-database.
 * User input is not modified and used directly in the HQL query.
 *
 * @author Dominik Schadow
 */
@WebServlet(name = "HQLServlet", urlPatterns = {"/HQLServlet"})
public class HQLServlet extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final long serialVersionUID = 1L;
    private SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @PreDestroy
    public void destroy() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String name = request.getParameter("name");
        logger.info("Received " + name + " as POST parameter");

        Session session = sessionFactory.openSession();
        Query query = session.createQuery("FROM Customer WHERE name = :name ORDER BY CUST_ID");
        query.setParameter("name", name);
        @SuppressWarnings("unchecked")
		List<Customer> customers = query.list();

        response.setContentType("text/html");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head>");
            out.println("<title>SQL Injection - Hibernate Query Language</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"resources/css/styles.css\" />");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>SQL Injection - Hibernate Query Language</h1>");
            out.println("<p><strong>Input</strong> " + name + "</p>");
            out.println("<h2>Customer Data</h2>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Name</th>");
            out.println("<th>Status</th>");
            out.println("<th>Order Limit</th>");
            out.println("</tr>");

            for (Customer customer : customers) {
                out.println("<tr>");
                out.println("<td>" + customer.getCustId() + "</td>");
                out.println("<td>" + customer.getName() + "</td>");
                out.println("<td>" + customer.getStatus() + "</td>");
                out.println("<td>" + customer.getOrderLimit() + "</td>");
                out.println("</tr>");
            }

            out.println("<table>");
            out.println("<p><a href=\"index.jsp\">Home</a></p>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
