package jp.chang.myclinic.reception.converter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

class ConverterBase {
    private List<String> errors = new ArrayList<>();

    public boolean hasError(){
        return errors.size() > 0;
    }

    public List<String> getErrors(){
        return errors;
    }

    protected void addError(String message){
        errors.add(message);
    }

    protected void convertToValidFrom(LocalDate src, String name, Consumer<String> cb){
        if( src == null || src == LocalDate.MAX ){
            errors.add(name + "の値が不適切です。");
        } else {
            cb.accept(src.toString());
        }
    }

    protected void convertToValidUpto(LocalDate src, String name, Consumer<String> cb){
        if( src == null ) {
            errors.add(name + "の値が不適切です。");
        } else if( src == LocalDate.MAX ){
            cb.accept("0000-00-00");
        } else {
            cb.accept(src.toString());
        }
    }

    protected void convertToInt(String src, String name, Function<Integer, String> check, Consumer<Integer> cb){
        if( src == null || src.isEmpty() ){
            addError(name + "の値が入力されていません。");
        } else {
            try {
                Integer value = Integer.parseInt(src);
                String err = check.apply(value);
                if (err == null) {
                    cb.accept(value);
                } else {
                    addError(err);
                }
            } catch (NumberFormatException ex) {
                addError(name + "の値が数値でありません。");
            }
        }
    }

    protected void convertToPositiveInt(String src, String name, Consumer<Integer> cb){
        convertToInt(src, name, ival -> {
            if( !(ival > 0) ){
                return name + "の値が正の値でありません。";
            } else {
                return null;
            }
        }, cb);
    }

    protected void validPeriodCheck(String validFrom, String validUpto, String validFromName, String validUptoName){
        if( validFrom != null && validUpto != null ){
            if( !validUpto.equals("0000-00-00") ){
                if( validFrom.compareTo(validUpto) > 0 ){
                    errors.add(String.format("%sが%sよりも前に日付になっています。", validFromName, validUptoName));
                }
            }
        }
    }

}
