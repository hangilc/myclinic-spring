package jp.chang.myclinic.reception.converter;

import jp.chang.myclinic.util.verify.KouhiVerifier;

import java.time.LocalDate;
import java.util.function.Consumer;

public class KouhiConverter extends ConverterBase {

    public void convertToFutansha(String src, Consumer<Integer> cb){
        String err = KouhiVerifier.verifyKouhiFutanshaBangouInput(src, cb);
        if( err != null ){
            addError(err);
        }
    }

    public void convertToJukyuusha(String src, Consumer<Integer> cb){
        String err = KouhiVerifier.verifyJukyuushaBangouInput(src, cb);
        if( err != null ){
            addError(err);
        }
    }

    public void convertToValidFrom(LocalDate src, Consumer<String> cb){
        convertToValidFrom(src, "資格取得日", cb);
    }

    public void convertToValidUpto(LocalDate src, Consumer<String> cb){
        convertToValidUpto(src, "有効期限", cb);
    }
}
