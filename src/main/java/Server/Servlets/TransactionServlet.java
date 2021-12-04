package Server.Servlets;

import DataBase.DBCPDataSource;
import DataBase.EventsJDBC;
import DataBase.SessionsJDBC;
import Server.LoginServerConstants;
import UI.AccountPage;
import UI.TransactionPage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionServlet extends HttpServlet {

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

                ResultSet transactions = EventsJDBC.executeDisplayUserTransactions(connection, userEmail);
                resp.getWriter().println(TransactionPage.displayTransactions(transactions));

            }catch (SQLException e){
                e.printStackTrace();
            }


        }else{
            // ask the user to login
            resp.getWriter().println(TransactionPage.RETURN_TO_LANDING);
        }
    }
}
