package jp.chang.myclinic.reception.converter;

import jp.chang.myclinic.dto.KoukikoureiDTO;

import java.time.LocalDate;
import java.util.function.Consumer;

public class KoukikoureiConverter extends ConverterBase {

    private static String validFromName = "資格取得日";
    private static String validUptoName = "有効期限";

    public void convertToHokenshaBangou(String src, Consumer<String> cb){
        if( src == null || src.isEmpty() ){
            addError("保険者番号が入力されていません。");
        } else {
            cb.accept(src);
        }
    }

    public void convertToHihokenshaBangou(String src, Consumer<String> cb){
        if( src == null || src.isEmpty() ){
            addError("被保険者番号が入力されていません。");
        } else {
            cb.accept(src);
        }
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
