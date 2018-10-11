package jp.chang.myclinic.util.value;

import jp.chang.myclinic.util.verify.HokenVerifierLib;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class Validators {

    private Validators() {

    }

    public static <T> Validator<T> isNotNull() {
        return (value, name, em) -> {
            if (value == null) {
                em.add(String.format("%sが設定されていません。", name));
            }
        };
    }

    public static Validator<String> isNotEmpty() {
        return (value, name, em) -> {
            if (value == null || value.isEmpty()) {
                em.add(String.format("%sが空白です。", name));
            }
        };
    }

    public static Validator<Integer> isPositive() {
        return (value, name, em) -> {
            if (!(value > 0)) {
                em.add(String.format("%sの値が正の数値でありません。", name));
            }
        };
    }

    public static Validator<Integer> inRange(int lo, int hi) {
        return (value, name, em) -> {
            if (!(value >= lo && value <= hi)) {
                em.add(String.format("%sの値が適切な範囲内でありません。", name));
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
            em.add(String.format("%sの値が適切でありません。", name));
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
                em.add(String.format("%sの桁数が少なすぎます。", name));
            } else if (ncol > hi) {
                em.add(String.format("%sの桁数が多すぎます。", name));
            }
        };
    }

    public static Validator<Integer> isValidHokenshaBangou() {
        return (value, name, em) -> {
            if (!(HokenVerifierLib.verifyHokenshaBangou(value))) {
                em.add(String.format("%sの検証番号が正しくありません。", name));
            }
        };
    }

}
