package util;

/**
 * A class to maintain info about landing uri.
 */
public class LandingUri {

    private String landing_uri;

    /**
     * Constructor
     * @param landing_uri
     */
    public LandingUri(String landing_uri){
        this.landing_uri = landing_uri;
    }


    /**
     * return landing_uri
     * @return
     */
    public String getLandingUri() {
        return this.landing_uri;
    }
}
