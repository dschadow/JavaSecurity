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
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String currentSessionId = request.getSession().getId();

        logger.info("Current session ID {}", currentSessionId);

        // This method actually returns the new session ID right away but to proof the update we request it manually
        request.changeSessionId();

        String newSessionId = request.getSession().getId();

        logger.info("New session ID {}", newSessionId);

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
            logger.error(ex.getMessage(), ex);
        }
    }
}
