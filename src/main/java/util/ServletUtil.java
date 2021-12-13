package util;


/**
 * A utility class with several helper methods for servlets.
 */
public class ServletUtil {


    /**
     * To return value of a parameter in post request body
     * @param bodyPart
     * @return
     */
    public static String getBodyParameter(String bodyPart){
        String[] parsedBodyPart = bodyPart.split("=");
        if(parsedBodyPart.length == 2){
            return parsedBodyPart[1];
        }else{
            return "";
        }

    }

    /**
     * To return value of a id related parameter in a post request body
     * @param part
     * @return
     */
    public static String getId(String part) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < part.length(); i++) {
            if (Character.isDigit(part.charAt(i))) {
                res.append(part.charAt(i));
            } else {
                break;
            }
        }
        return res.toString();
    }

}
