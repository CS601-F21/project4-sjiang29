package Util;

/**
 * A class to maintain info about each client.
 */
public class ClientInfo {

    private String name;
    private String email;

    /**
     * Constructor
     * @param name
     */
    public ClientInfo(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * return name
     * @return
     */
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
