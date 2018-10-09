package jp.chang.myclinic.util.value;

public class Converters {

    private Converters() {

    }

    public static Converter<String, Integer> stringToIntegerConverter() {
        return (src, name, em) -> {
            if (src == null || src.isEmpty()) {
                em.add(String.format("%sが設定されていません。", name));
                return null;
            }
            try {
                return Integer.parseInt(src);
            } catch (NumberFormatException ex) {
                em.add(String.format("%sが数値でありません。", name));
                return null;
            }
        };
    }

    public static <T> Converter<T, T> nonNullConverter(){
        return (src, name, em) -> {
            if( src == null ){
                em.add(String.format("%sが null です。", name));
                return null;
            }
            return src;
        };
    }

    public static Converter<Integer, Integer> integerRangeConverter(int low, int hi){
        return (src, name, em) -> {
            if( src == null ){
                em.add(String.format("Null source. (%s)", name));
                return null;
            }
            if( src >= low && src <= hi ){
                return src;
            } else {
                em.add(String.format("%sが適切な範囲内でありません。", name));
                return null;
            }
        };
    }

    public static Converter<Integer, Integer> columnDigitsRangeConverter(int low, int hi){
        return (src, name, em) -> {
            if( src == null ){
                em.add(String.format("Null source. (%s)", name));
                return null;
            }
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
