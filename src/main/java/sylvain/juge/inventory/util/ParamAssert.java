package sylvain.juge.inventory.util;

public class ParamAssert {

    private ParamAssert(){
        // un-callable constructor
    }

    public static void notNull(Object o, String name){
        if( null == o ){
            throw new InvalidParameterException(name, "null value not allowed");
        }
    }

}
