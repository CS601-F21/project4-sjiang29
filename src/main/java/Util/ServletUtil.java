package Util;

public class ServletUtil {

    public static String getBodyParameter(String bodyPart){
        String[] parsedBodyPart = bodyPart.split("=");
        if(parsedBodyPart.length == 2){
            return parsedBodyPart[1];
        }else{
            return "";
        }

    }

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
