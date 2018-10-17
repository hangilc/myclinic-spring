package jp.chang.myclinic.util.logic;

public class LogicUtil {

    public static String nameWith(String name, String append){
        if( name == null ){
            return "";
        } else {
            return name + append;
        }
    }

}
