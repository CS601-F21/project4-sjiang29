package Util;

public class ServeletUtil {

    public static String getBodyParameter(String bodyPart){
        String[] parsedBodyPart = bodyPart.split("=");
        return parsedBodyPart[1];
    }

}
