package server.servlets;

import server.LoginServletConstants;
import server.ServerConstants;
import ui.LandingPage;
import util.Config;
import util.LoginUtilities;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;


import java.io.IOException;
import java.io.PrintWriter;
import static server.HttpServer.LOGGER;

/**
 * Landing page that allows a user to request to login with Slack.
 * Referenced from cs 601 example code
 */
public class LandingServlet extends HttpServlet {


    /**
     * Reference code from cs 601 Example code
     * Implement logic to deal get request sent to /
     * @param req
     * @param resp
     *
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServletConstants.CLIENT_INFO_KEY);
        LOGGER.info("landing---get client info");
        String uri = req.getRequestURI();
        LOGGER.info("uri from landing:" + uri);
        if(!ServerConstants.PATHS.contains(uri)){
            resp.setStatus(HttpStatus.NOT_FOUND_404);
            resp.getWriter().println("Page not Found 404");
            return;
        }

        if(clientInfoObj != null) {
            // already authed, no need to log in
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LandingPage.ALREADY_AUTHENTICATED);
            return;
        }

        // retrieve the config info from the context
        Config config = (Config) req.getServletContext().getAttribute(LoginServletConstants.CONFIG_KEY);

        /** From the OpenID spec:
         * state
         * RECOMMENDED. Opaque value used to maintain state between the request and the callback.
         * Typically, Cross-Site Request Forgery (CSRF, XSRF) mitigation is done by cryptographically
         * binding the value of this parameter with a browser cookie.
         *
         * Use the session ID for this purpose.
         */
        String state = sessionId;

        /** From the Open ID spec:
         * nonce
         * OPTIONAL. String value used to associate a Client session with an ID Token, and to mitigate
         * replay attacks. The value is passed through unmodified from the Authentication Request to
         * the ID Token. Sufficient entropy MUST be present in the nonce values used to prevent attackers
         * from guessing values. For implementation notes, see Section 15.5.2.
         */
        String nonce = LoginUtilities.generateNonce(state);

        // Generate url for request to Slack
        String url = LoginUtilities.generateSlackAuthorizeURL(config.getClient_id(),
                state,
                nonce,
                config.getRedirect_url());

        resp.setStatus(HttpStatus.OK_200);
        PrintWriter writer = resp.getWriter();
        LOGGER.info("landing successfully");
        writer.println(LandingPage.getLandingPage(url));

    }


}
