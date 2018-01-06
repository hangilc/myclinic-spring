package jp.chang.myclinic.reception.converter;

import java.time.LocalDate;
import java.util.function.Consumer;

public class KouhiConverter extends ConverterBase {

    public void convertToFutansha(String src, Consumer<Integer> cb){
        convertToPositiveInt(src, "負担者番号", cb);
    }

    public void convertToJukyuusha(String src, Consumer<Integer> cb){
        convertToPositiveInt(src, "受給者番号", cb);
    }

    public void convertToValidFrom(LocalDate src, Consumer<String> cb){
        convertToValidFrom(src, "資格取得日", cb);
    }

    public void convertToValidUpto(LocalDate src, Consumer<String> cb){
        convertToValidUpto(src, "有効期限", cb);
    }
}
