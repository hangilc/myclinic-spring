package jp.chang.myclinic.reception.lib;

import jp.chang.myclinic.dto.ShahokokuhoDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class ShahokokuhoConverter {

    public void convertToHokenshaBangou(String src, List<String> errors, Consumer<Integer> cb){
        if( src == null || src.isEmpty() ){
            errors.add("保険者番号が入力されていません。");
        }
        try {
            int value = Integer.parseInt(src);
            if( value > 0 ){
                cb.accept(value);
            } else {
                errors.add("保険者番号が正の値でありません。");
            }
        } catch(NumberFormatException ex){
            errors.add("保険者番号の値が不適切です。");
        }
    }

    public void convertToHihokenshaKigou(String src, List<String> errors, Consumer<String> cb){
        cb.accept(src == null ? "" : src);
    }

    public void convertToHihokenshaBangou(String src, List<String> errors, Consumer<String> cb){
        cb.accept(src == null ? "" : src);
    }

    public void convertToHonninKazoku(Integer honnin, List<String> errors, Consumer<Integer> cb){
        if( honnin == 0 || honnin == 1 ){
            cb.accept(honnin);
        } else {
            errors.add("本人・家族の値が不適切です。");
        }
    }

    public void convertToValidFrom(LocalDate src, List<String> errors, Consumer<String> cb){
        if( src == null || src == LocalDate.MAX ){
            errors.add("資格取得日が不適切です。");
        } else {
            cb.accept(src.toString());
        }
    }

    public void convertToValidUpto(LocalDate src, List<String> errors, Consumer<String> cb){
        if( src == null ) {
            errors.add("有効期限が不適切です。");
        } else if( src == LocalDate.MAX ){
            cb.accept("0000-00-00");
        } else {
            cb.accept(src.toString());
        }
    }

    public void convertToKourei(Integer src, List<String> errors, Consumer<Integer> cb){
        if( src != null && src >= 0 && src <= 3 ){
            cb.accept(src);
        } else {
            errors.add("高齢の設定が不適切です。");
        }
    }

    public void integrationCheck(ShahokokuhoDTO data, List<String> errors){
        if( data.hihokenshaKigou != null && data.hihokenshaBangou != null ){
            if( data.hihokenshaKigou.isEmpty() && data.hihokenshaBangou.isEmpty() ){
                errors.add("被保険者記号と被保険者番号が両方空白です。");
            }
        }
        if( data.validFrom != null && data.validUpto != null ){
            if( !data.validUpto.equals("0000-00-00") ){
                if( data.validFrom.compareTo(data.validUpto) > 0 ){
                    errors.add("資格取得日が有効期限よりも前に日付になっています。");
                }
            }
        }
    }

}
