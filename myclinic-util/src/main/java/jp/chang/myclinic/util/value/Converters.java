package jp.chang.myclinic.util.value;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Objects;

public class Converters {

    private Converters() {

    }

    public static Converter<String, Integer> stringToInteger() {
        return (src, name, em) -> {
            try {
                return Integer.parseInt(src);
            } catch (NumberFormatException ex) {
                em.add(String.format("%sが数値でありません。", name));
                return null;
            }
        };
    }

    public static Converter<Integer, Integer> columnDigitsRange(int low, int hi){
        return (src, name, em) -> {
            if( src < 0 ){
                src = -src;
            }
            int ncol = String.format("%d", src).length();
            if( ncol < low ){
                em.add(String.format("%sの桁数が少なすぎます。", name));
                return null;
            }
            if( ncol > hi ){
                em.add(String.format("%sの桁数が多すぎます。", name));
                return null;
            }
            return src;
        };
    }

    public static Converter<LocalDate, String> dateToString(){
        return (date, name, em) -> {
            if( date == null ){
                return null;
            } else {
                return date.toString();
            }
        };
    }

    public static Converter<String, LocalDate> stringToDate(){
        return (str, name, em) -> {
            if( str == null ){
                return null;
            } else {
                try {
                    if( str.length() > 10 ){
                        str = str.substring(0, 10);
                    }
                    return LocalDate.parse(str);
                } catch(DateTimeException ex){
                    em.add(String.format("%sが正しい日付でありません。", name));
                    return null;
                }
            }
        };
    }

    public static Converter<LocalDate, String> validUptoToString(){
        return (date, name, em) -> {
            if( date == null ){
                return null;
            } else {
                if( Objects.equals(LocalDate.MAX, date) ){
                    return "0000-00-00";
                } else {
                    return date.toString();
                }
            }
        };
    }

    public static Converter<String, LocalDate> stringToValidUpto(){
        return (str, name, em) -> {
            if( str == null ){
                return null;
            } else {
                try {
                    if( str.length() > 10 ){
                        str = str.substring(0, 10);
                    }
                    if( "0000-00-00".equals(str) ){
                        return LocalDate.MAX;
                    } else {
                        return LocalDate.parse(str);
                    }
                } catch(DateTimeException ex){
                    em.add(String.format("%sが正しい日付でありません。", name));
                    return null;
                }
            }
        };
    }

    public static Converter<String, String> nullToEmpty(){
        return (str, name, em) -> str == null ? "" : str;
    }

    public static Converter<Integer, Integer> nullToZero(){
        return (value, name, em) -> value == null ? 0 : value;
    }

}
