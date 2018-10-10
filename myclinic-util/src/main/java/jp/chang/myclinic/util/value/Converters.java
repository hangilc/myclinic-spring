package jp.chang.myclinic.util.value;

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

    public static Converter<Integer, Integer> columnDigitsRangeConverter(int low, int hi){
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

}
