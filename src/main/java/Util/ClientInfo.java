package Util;

/**
 * A class to maintain info about each client.
 * Referenced from cs 601 example code
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

    /**
     * return email
     * @return
     */
    public String getEmail() {
        return email;
    }
}
