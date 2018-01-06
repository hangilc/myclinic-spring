package jp.chang.myclinic.reception.converter;

import jp.chang.myclinic.dto.KoukikoureiDTO;

import java.time.LocalDate;
import java.util.function.Consumer;

public class RoujinConverter extends ConverterBase {

    private static String validFromName = "資格取得日";
    private static String validUptoName = "有効期限";

    public void convertToShichouson(String src, Consumer<Integer> cb){
        convertToPositiveInt(src, "市町村番号", cb);
    }

    public void convertToJukyuusha(String src, Consumer<Integer> cb){
        convertToPositiveInt(src, "受給者番号", cb);
    }

    public void convertToValidFrom(LocalDate src, Consumer<String> cb){
        super.convertToValidFrom(src, validFromName, cb);
    }

    public void convertToValidUpto(LocalDate src, Consumer<String> cb){
        super.convertToValidUpto(src, validUptoName, cb);
    }

    public void convertToFutanWari(int src, Consumer<Integer> cb){
        if( src >= 1 && src <= 3 ){
            cb.accept(src);
        } else {
            addError("負担割の値が不適切です。");
        }
    }

    public void integralCheck(KoukikoureiDTO data){
        validPeriodCheck(data.validFrom, data.validUpto, validFromName, validUptoName);
    }

}
