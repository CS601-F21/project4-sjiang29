package Server;

import java.util.Arrays;
import java.util.HashSet;

public class ServerConstants {

    public static final HashSet<String> PATHS = new HashSet<>(Arrays.asList(ServerConstants.PATH_TO_LANDING,
            ServerConstants.PATH_TO_ACCOUNT, ServerConstants.PATH_TO_TRANSACTIONS,
            ServerConstants.PATH_TO_EVENTS, ServerConstants.PATH_TO_TICKETS, ServerConstants.PATH_TO_MYTICKETS,
            ServerConstants.PATH_TO_NEWEVENT, ServerConstants.PATH_TO_MYEVENTS, ServerConstants.PATH_TO_LOGIN,
            ServerConstants.PATH_TO_NEWTICKET, ServerConstants.PATH_TO_LOGOUT));

    public static final String PATH_TO_LANDING = "/";
    public static final String PATH_TO_ACCOUNT = "/account";
    public static final String PATH_TO_TRANSACTIONS = "/transactions";
    public static final String PATH_TO_EVENTS = "/events";
    public static final String PATH_TO_TICKETS = "/tickets";
    public static final String PATH_TO_MYTICKETS = "/myTickets";
    public static final String PATH_TO_NEWTICKET = "/newTicket";
    public static final String PATH_TO_NEWEVENT = "/newEvent";
    public static final String PATH_TO_MYEVENTS = "/myEvents";
    public static final String PATH_TO_LOGIN = "/login";
    public static final String PATH_TO_LOGOUT = "/logout";




}
