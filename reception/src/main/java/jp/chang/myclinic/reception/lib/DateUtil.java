package jp.chang.myclinic.reception.lib;

import jp.chang.myclinic.consts.Gengou;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseEra;

public class DateUtil {

    public static Result<LocalDate> convertToLocalDate(Gengou gengou, String nen, String month, String day){
        Result<LocalDate> result = new Result<>();
        JapaneseEra era = gengou.getEra();
        int nenValue = 0, monthValue = 0, dayValue = 9;
        try {
            nenValue = Integer.parseInt(nen);
        } catch(NumberFormatException ex){
            result.addError("年の値が適切でありません。");
        }
        try {
            monthValue = Integer.parseInt(month);
        } catch(NumberFormatException ex){
            result.addError("月の値が適切でありません。");
        }
        try {
            dayValue = Integer.parseInt(day);
        } catch(NumberFormatException ex){
            result.addError("日の値が適切でありません。");
        }
        if( !result.hasError() ){
            int year = JapaneseChronology.INSTANCE.prolepticYear(era, nenValue);
            try {
                result.value = LocalDate.of(year, monthValue, dayValue);
            } catch(DateTimeException ex){
                result.addError("入力した日付が不適切です。");
            }
        }
        return result;
    }

}
