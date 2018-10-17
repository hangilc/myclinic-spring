package jp.chang.myclinic.util.logic;

import jp.chang.myclinic.util.verify.HokenVerifierLib;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class Validators extends LogicUtil {

    private Validators() {

    }

    public static <T> void valid(T value, String name, ErrorMessages em) {
        // do nothing
    }

//    public static <T> Validator<T> valid(){
//        return (value, name, em) -> {};
//    }

    public static <T> void isNotNull(T value, String name, ErrorMessages em) {
        if (value == null) {
            em.add(nameWith(name, "が") + "設定されていません。");
        }
    }

//    public static <T> Validator<T> isNotNull() {
//        return (value, name, em) -> {
//            if (value == null) {
//                em.add(String.format("%sが設定されていません。", name));
//            }
//        };
//    }

    public static void isNotEmpty(String value, String name, ErrorMessages em) {
        if (value.isEmpty()) {
            em.add(nameWith(name, "が") + "空白です。");
        }

    }

//    public static Validator<String> isNotEmpty() {
//        return (value, name, em) -> {
//            if (value.isEmpty()) {
//                em.add(String.format("%sが空白です。", name));
//            }
//        };
//    }

    public static void isPositive(Integer value, String name, ErrorMessages em) {
        if (!(value > 0)) {
            em.add(nameWith(name, "が") + "正の数値でありません。");
        }
    }

//    public static Validator<Integer> isPositive() {
//        return (value, name, em) -> {
//            if (!(value > 0)) {
//                em.add(String.format("%sの値が正の数値でありません。", name));
//            }
//        };
//    }

    public static Validator<Integer> isInRange(int lo, int hi) {
        return (value, name, em) -> {
            if (!(value >= lo && value <= hi)) {
                em.add(nameWith(name, "の") + "値が適切な範囲内でありません。");
            }
        };
    }

    public static Validator<Integer> isEqualOrGreaterThan(int lo){
        return (value, name, em) -> {
            if( !(value >= lo) ){
                em.add(String.format("%s値が %d 以上でありません。", nameWith(name, "の"), lo));
            }
        };
    }

    public static <T> Validator<T> isOneOf(Collection<T> set) {
        return (value, name, em) -> {
            for (T t : set) {
                if (Objects.equals(t, value)) {
                    return;
                }
            }
            em.add(nameWith(name, "の") + "値が適切でありません。");
        };
    }

    public static Validator<Integer> isOneOf(Integer... set) {
        return isOneOf(Arrays.asList(set));
    }

    public static Validator<Integer> hasDigitsInRange(int lo, int hi) {
        return (value, name, em) -> {
            if (value < 0) {
                value = -value;
            }
            int ncol = String.format("%d", value).length();
            if (ncol < lo) {
                em.add(nameWith(name, "の") + "桁数が少なすぎます。");
            } else if (ncol > hi) {
                em.add(nameWith(name, "の") + "桁数が多すぎます。");
            }
        };
    }

    public static void hasValidCheckingDigit(Integer value, String name, ErrorMessages em){
        if (!(HokenVerifierLib.verifyHokenshaBangou(value))) {
            em.add(nameWith(name, "の") + "検証番号が正しくありません。");
        }
    }

//    public static Validator<Integer> isValidHokenshaBangou() {
//        return (value, name, em) -> {
//            if (!(HokenVerifierLib.verifyHokenshaBangou(value))) {
//                em.add(String.format("%sの検証番号が正しくありません。", name));
//            }
//        };
//    }

//    public static void verifyValidInterval(String validFromStore, String validUptoStore,
//                                           String validFromName, String validUptoName,
//                                           ErrorMessages em) {
//        new BiLogicValue<>(validFromStore, validUptoStore)
//                .validate(isNotNull(), valid())
//                .convert(sqldateToLocalDate())
//                .validate(validInterval())
//                .getValues(validFromName, validUptoName, em);
//    }

}
