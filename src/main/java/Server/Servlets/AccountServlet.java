package Server.Servlets;

import DataBase.DBCPDataSource;
import DataBase.SessionsJDBC;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

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
            resp.getWriter().println(AccountPage.getUserInfo(userName,userEmail,zipcode));
            //resp.getWriter().println(AccountPage.PAGE_FOOTER);
            //return;
        }else{

            // ask the user to login

        }



    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

    }
}
