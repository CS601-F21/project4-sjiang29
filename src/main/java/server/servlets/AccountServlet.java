package server.servlets;

import database.DBCPDataSource;
import database.SessionsJDBC;
import database.UsersJDBC;
import server.LoginServletConstants;
import ui.AccountPage;
import ui.UIConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static server.HttpServer.LOGGER;

/**
 * Implements logic for the /account path
 *
 */
public class AccountServlet extends HttpServlet {


    /**
     * Implement logic to deal get request sent to /account
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

        if(clientInfoObj != null) {
            // already authed, no need to log in
            resp.setStatus(HttpStatus.OK_200);
            String userName = "";
            String userEmail = "";
            int zipcode = 0;
            try (Connection connection = DBCPDataSource.getConnection()){
                ResultSet user = SessionsJDBC.executeSelectUserBySessionId(connection, sessionId);
                if(user.next()){
                    userName = user.getString("name");
                    userEmail = user.getString("email");
                    zipcode = user.getInt("zipcode");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            resp.getWriter().println(AccountPage.getAccountPage(userName, userEmail, zipcode));

        }else{
            // ask the user to login
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(UIConstants.RETURN_TO_LANDING);
        }
    }


    /**
     * Implement logic to deal post request sent to /account
     * @param req
     * @param resp
     *
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServletConstants.CLIENT_INFO_KEY);

        if(clientInfoObj != null) {
            resp.setStatus(HttpStatus.OK_200);
            try(BufferedReader reader = req.getReader()) {
                String body = URLDecoder.decode(reader.readLine(), StandardCharsets.UTF_8.toString());
                LOGGER.info("body: " + body);
                updateAccount(body, resp.getWriter(), sessionId);
            }
        }else{
            // ask the user to login
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(UIConstants.RETURN_TO_LANDING);
        }
    }


    /**
     * Private helper method to deal post request relating to update account
     * @param body post request's body
     * @param writer
     * @param sessionId
     *
     */
    private static void updateAccount(String body, PrintWriter writer, String sessionId){
        String[] bodyParts = body.split("&");

        String userNamePart = bodyParts[0];
        String[] parsedUserName = userNamePart.split("=");
        String newUserName = "";
        if(parsedUserName.length == 2){
            newUserName = parsedUserName[1];
        }

        LOGGER.info("new user name: " + newUserName);

        String zipcodePart = bodyParts[1];
        String[] parsedZipcode = zipcodePart.split("=");
        String inputZipcode = "";
        if(parsedZipcode.length == 2){
            inputZipcode = parsedZipcode[1];
        }

        LOGGER.info("new zipcode: " + inputZipcode);

        int newZipcode = 0;
        if(inputZipcode != ""){
            newZipcode = Integer.parseInt(inputZipcode);
        }

        String userEmail = "";
        String userName = "";
        int zipcode = 0;
        String updatedName = "";
        int updatedZipcode = 0;
        try (Connection connection = DBCPDataSource.getConnection()){
            ResultSet user = SessionsJDBC.executeSelectUserBySessionId(connection, sessionId);
            if(user.next()){
                userName = user.getString("name");
                userEmail = user.getString("email");
                zipcode = user.getInt("zipcode");
            }
            if(newUserName.equals("")){
                newUserName = userName;
            }
            if(newZipcode == 0){
                newZipcode = zipcode;
            }

            UsersJDBC.executeUpdateUser(connection,userEmail, newUserName, newZipcode);
            ResultSet updatedUser = UsersJDBC.executeSelectUserByEmail(connection, userEmail);
            if(updatedUser.next()){
                updatedName = updatedUser.getString("name");
                LOGGER.info("updated username from db:" + updatedName);
                updatedZipcode = updatedUser.getInt("zipcode");
                LOGGER.info("updated zipcode from db: " + updatedZipcode);
            }
            writer.println(AccountPage.postAccountPage(updatedName, userEmail, updatedZipcode));

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
