package util;


/**
 * A class to maintain response from slack
 */
public class ResponseFromSlack {
    private String ok;

    /**
     * Constructor
     * @param ok
     */
    public ResponseFromSlack(String ok) {
        this.ok = ok;
    }

    /**
     * return ok
     * @return
     */
    public String getOk() {
        return ok;
    }
}
