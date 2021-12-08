package Util;

/**
 * Helper class to store information needed in request body sent to slack api.
 */
public class SlackRequestBody {

    private String channel;
    private String text;

    /**
     * Constructor for SlackRequestBody
     * @param channel
     * @param text
     */
    public SlackRequestBody(String channel, String text){
        this.channel = channel;
        this.text = text;
    }

    /**
     * Get and return channel
     * @return
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Get and return text
     * @return
     */
    public String getText() {
        return text;
    }
}
