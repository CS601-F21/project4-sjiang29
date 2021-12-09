package Server;

import java.util.Arrays;
import java.util.HashSet;

public class ServerConstants {

    public static final HashSet<String> PATHS = new HashSet<>(Arrays.asList("/events", "/account", "/"));
    public static final String PATH_TO_ACCOUNT = "/account";
    public static final String PATH_TO_TRANSACTIONS = "/transactions";
    public static final String PATH_TO_EVENTS = "/events";

}
