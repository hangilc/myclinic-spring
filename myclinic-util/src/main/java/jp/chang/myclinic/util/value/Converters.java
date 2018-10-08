package jp.chang.myclinic.util.value;

public class Converters {

    private Converters() {

    }

    public static ValueConverter<String, Integer> integerFromString(String name){
        return (s, em) -> {
            if( s == null || s.isEmpty() ){
                em.add(String.format("%sが設定されていません。", name));
                return null;
            }
            try {
                return Integer.parseInt(s);
            } catch(NumberFormatException ex){
                em.add(String.format("%sが数値でありません。", name));
                return null;
            }
        };
    }

    public static ValueConverter<Integer, Integer> positiveInteger(String name){
        return (i, em) -> {
            if( !() ){

            }
        }
    }

}
