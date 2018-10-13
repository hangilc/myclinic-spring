package jp.chang.myclinic.util.logic;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Converters {

    public static Converter<String, Integer> stringToInteger(){
        return (value, name, em) -> {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                em.add(String.format("%sが数値でありません。", name));
                return null;
            }
        };
    }

    public static Converter<Integer, String> integerToString(){
        return (value, name, em) -> String.format("%d", value);
    }

    public static Converter<String, LocalDate> sqldateToLocalDate(){
        return (value, name, em) -> {
            if( value == null || "0000-00-00".equals(value) ){
                return null;
            }
            try {
                return LocalDate.parse(value);
            } catch(DateTimeException ex){
                em.add(String.format("%sが日付でありません。", name));
                return null;
            }
        };
    }

    public static Converter<LocalDate, String> localDateToSqldate(){
        return (value, name, em) -> {
            if( value == null ){
                return "0000-00-00";
            } else {
                return value.toString();
            }
        };
    }

}
