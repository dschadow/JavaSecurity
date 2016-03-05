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
package de.dominikschadow.javasecurity.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebListener
public class ConnectionListener implements ServletContextListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionListener.class);
    public static Connection con = null;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            con = DriverManager.getConnection("jdbc:h2:mem:SQL-Injection-DB", "sa", "");
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
