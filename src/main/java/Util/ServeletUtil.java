package Util;

public class ServeletUtil {

    public static String getBodyParameter(String bodyPart){
        String[] parsedBodyPart = bodyPart.split("=");
        if(parsedBodyPart.length == 2){
            return parsedBodyPart[1];
        }else{
            return "";
        }

    }

}
