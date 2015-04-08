package de.dominikschadow.javasecurity.listener;

import org.flywaydb.core.Flyway;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class FlywayListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:h2:file:~/SQL-Injection-DB", "sa", "");
        flyway.migrate();
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
