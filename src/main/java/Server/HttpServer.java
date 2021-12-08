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
        context.addServlet(LandingServlet.class, "/");

        // Once authenticated, Slack will redirect the user
        // back to /login
        context.addServlet(LoginServlet.class, "/login");

        context.addServlet(AccountServlet.class, "/account");
        context.addServlet(TransactionServlet.class, "/transaction");
        context.addServlet(EventsServlet.class, "/events");

        context.addServlet(TicketsServlet.class, "/tickets");

        context.addServlet(MyTicketsServlet.class, "/myTickets");
        context.addServlet(NewEventServlet.class, "/newEvent");
        context.addServlet(MyOwnEventsServlet.class, "/myOwnEvents");

        // handle logout
        context.addServlet(LogoutServlet.class, "/logout");

        // start it up!
        server.setHandler(context);
        server.start();
    }
}
