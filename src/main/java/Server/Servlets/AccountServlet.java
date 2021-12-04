package Server.Servlets;

import DataBase.DBCPDataSource;
import DataBase.SessionsJDBC;
import DataBase.UsersJDBC;
import Server.LoginServerConstants;
import UI.AccountPage;
import Util.ClientInfo;
import Util.Config;
import Util.HttpFetcher;
import Util.LoginUtilities;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;
import static Server.HttpServer.LOGGER;

public class AccountServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in

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

            //resp.getWriter().println(AccountPage.PAGE_HEADER);
            resp.getWriter().println(AccountPage.getAccountPage(userName, userEmail, zipcode));
            //resp.getWriter().println(AccountPage.PAGE_FOOTER);
            //return;
        }else{

            // ask the user to login
            resp.getWriter().println(AccountPage.RETURN_TO_LANDING);

        }



    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {

            try(BufferedReader reader = req.getReader()) {
                String body = URLDecoder.decode(reader.readLine(), StandardCharsets.UTF_8.toString());
                //TODO: verify the body exists and it contains a =
                LOGGER.info("body: " + body);
                String[] bodyParts = body.split("&");

                String userNamePart = bodyParts[0];
                String[] parsedUserName = userNamePart.split("=");
                String newUserName = parsedUserName[1];
                LOGGER.info("new user name: " + newUserName);

                String zipcodePart = bodyParts[1];
                String[] parsedZipcode = zipcodePart.split("=");
                String zipcode = parsedZipcode[1];
                LOGGER.info("new zipcode: " + zipcode);

                int newZipcode = 0;
                if(zipcode != ""){
                    newZipcode = Integer.parseInt(zipcode);
                }

                String userEmail = "";
                String updatedName = "";
                int updatedZipcode = 0;
                try (Connection connection = DBCPDataSource.getConnection()){
                    ResultSet user = SessionsJDBC.executeSelectUserBySessionId(connection, sessionId);
                    if(user.next()){
                        userEmail = user.getString("email");
                    }

                    UsersJDBC.executeUpdateUser(connection,userEmail,newUserName, newZipcode);
                    ResultSet updatedUser = UsersJDBC.executeSelectUserByEmail(connection, userEmail);
                    if(updatedUser.next()){
                        updatedName = updatedUser.getString("name");
                        LOGGER.info("updated username from db:" + updatedName);
                        updatedZipcode = updatedUser.getInt("zipcode");
                        LOGGER.info("updated zipcode from db: " + updatedZipcode);
                    }

                    resp.getWriter().println(AccountPage.postAccountPage(updatedName,userEmail, updatedZipcode));



                }catch (SQLException e){
                    e.printStackTrace();
                }



            }



        }else{

            // ask the user to login
            resp.getWriter().println(AccountPage.RETURN_TO_LANDING);

        }




    }
}
