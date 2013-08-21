package sylvain.juge.inventory.util;

public class InvalidParameterException extends RuntimeException {

    public InvalidParameterException(String paramName, String msg){
        super(String.format("invalid value for parameter %s : %s", paramName, msg));
    }
}
