package jp.chang.myclinic.util.logic;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Converters extends LogicUtil {

    public static Integer stringToInteger(String value, String name, ErrorMessages em){
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            em.add(nameWith(name, "が") + "数値でありません。");
            return null;
        }
    }

    public static LocalDate sqldateToLocalDate(String value, String name, ErrorMessages em){
        if( value == null || "0000-00-00".equals(value) ){
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch(DateTimeException ex){
            em.add(nameWith(name, "が") + "日付でありません。");
            return null;
        }
    }

}
