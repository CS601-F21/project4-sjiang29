package Server;

import Server.Servlets.*;
import Util.Config;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.FileReader;

public class HttpServer {

    public static final int PORT = 8080;
    private static final String configFilename = "config.json";
    public static final Logger LOGGER = LogManager.getLogger(HttpServer.class);

    public static void main(String[] args) {
        try {
            startup();
        } catch(Exception e) {
            // catch generic Exception as that is what is thrown by server start method
            e.printStackTrace();
        }
    }

    /**
     * A helper method to start the server.
     * @throws Exception -- generic Exception thrown by server start method
     */
    public static void startup() throws Exception {

        // read the client id and secret from a config file
        Gson gson = new Gson();
        Config config = gson.fromJson(new FileReader(configFilename), Config.class);
        String redirectUrl = config.getRedirect_url();
        String s = "redirect url is:";
        LOGGER.info(s);
        LOGGER.info(redirectUrl);

        // create a new server
        Server server = new Server(PORT);
        LOGGER.info("Server is running");

        // make the config information available across servlets by setting an
        // attribute in the context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setAttribute(LoginServletConstants.CONFIG_KEY, config);

        // the default path will direct to a landing page with
        // "Login with Slack" button
        context.addServlet(LandingServlet.class, ServerConstants.PATH_TO_LANDING);

        // Once authenticated, Slack will redirect the user
        // back to /login
        context.addServlet(LoginServlet.class, ServerConstants.PATH_TO_LOGIN);

        context.addServlet(AccountServlet.class, ServerConstants.PATH_TO_ACCOUNT);
        context.addServlet(TransactionServlet.class, ServerConstants.PATH_TO_TRANSACTIONS);
        context.addServlet(EventsServlet.class, ServerConstants.PATH_TO_EVENTS);

        context.addServlet(TicketsServlet.class, ServerConstants.PATH_TO_TICKETS);

        context.addServlet(MyTicketsServlet.class, ServerConstants.PATH_TO_MYEVENTS);
        context.addServlet(NewEventServlet.class, ServerConstants.PATH_TO_NEWEVENT);
        context.addServlet(MyEventsServlet.class, ServerConstants.PATH_TO_MYEVENTS);

        // handle logout
        context.addServlet(LogoutServlet.class, ServerConstants.PATH_TO_LOGOUT);

        // start it up!
        server.setHandler(context);
        server.start();
    }
}
