package util;

/**
 * A class parse necessary configuration info.
 * Referenced from cs 601 example code
 */
public class Config {

    // These variable names violate Java style guidelines
    // in order to be consistent with the naming conventions
    // in the Slack API

    private String redirect_uri;
    private String client_id;
    private String client_secret;

    public Config(String redirect_uri, String client_id, String client_secret) {
        this.redirect_uri = redirect_uri;
        this.client_id = client_id;
        this.client_secret = client_secret;
    }



    // return redirect_url
    public String getRedirect_url() {
        return redirect_uri;
    }

    // return client_id
    public String getClient_id() {
        return client_id;
    }

    // return client_secret
    public String getClient_secret() {
        return client_secret;
    }
}
