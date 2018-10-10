package jp.chang.myclinic.util.value;

import jp.chang.myclinic.util.verify.HokenVerifierLib;

import java.util.Collection;

public class Validators {

    private Validators() {

    }

    public static Validator<Integer> isPositive(){
        return (value, name, em) -> {
            if (!(value > 0)) {
                em.add(String.format("%sの値が正の数値でありません。", name));
            }
        };
    }

    public static Validator<Integer> inRange(int lo, int hi){
        return (value, name, em) -> {
            if( !(value >= lo && value <= hi) ){
                em.add(String.format("%sの値が適切な範囲内でありません。", name));
            }
        };
    }

    public static Validator<Integer> oneOf(Collection<Integer> set){
        return (value, name, em) -> {
            if( !(set.contains(value)) ){
                em.add(String.format("%sの値が適切でありません。", name));
            }
        };
    }

    public static Validator<Integer> columnDigitsInRange(int lo, int hi){
        return (value, name, em) -> {
            if (value < 0) {
                value = -value;
                int ncol = String.format("%d", value).length();
                if (ncol < lo) {
                    em.add(String.format("%sの桁数が少なすぎます。", name));
                } else if (ncol > hi) {
                    em.add(String.format("%sの桁数が多すぎます。", name));
                }
            }
        };
    }

    public static Validator<Integer> isValidHokenshaBangou(){
        return (value, name, em) -> {
            if( !(HokenVerifierLib.verifyHokenshaBangou(value)) ){
                em.add(String.format("%sの検証番号が正しくありません。", name));
            }
        };
    }

}
