package org.ninestar.crawling.adminui;

import com.vaadin.server.VaadinServlet;
import org.ninestar.crawling.commonui.Configuration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    private static final Boolean PRODUCTION_MODE = true;

    public static void main(String[] args) {
        int port = Configuration.INSTANCE.getInt("port", 8080);
        Server server = new Server(port);
        ServletContextHandler contextHandler
                = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        ServletHolder sh = new ServletHolder(new VaadinServlet());
        contextHandler.addServlet(sh, "/*");
        contextHandler.setInitParameter("ui", CrawlerAdminUI.class.getCanonicalName());
        contextHandler.setInitParameter("productionMode", String.valueOf(PRODUCTION_MODE));
        server.setHandler(contextHandler);
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            LOG.error("Failed to start application", e);
        }
    }
}