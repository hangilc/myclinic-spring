package jp.chang.myclinic.util.logic;

import jp.chang.myclinic.util.dto_logic.HokenLib;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class Validators extends LogicUtil {

    private Validators() {

    }

    public static <T> void valid(T value, String name, ErrorMessages em) {
        // do nothing
    }

    public static <T> void isNotNull(T value, String name, ErrorMessages em) {
        if (value == null) {
            em.add(nameWith(name, "が") + "設定されていません。");
        }
    }

    public static void isNotEmpty(String value, String name, ErrorMessages em) {
        if (value.isEmpty()) {
            em.add(nameWith(name, "が") + "空白です。");
        }
    }

    public static Validator<String> hasLength(int len) {
        return hasLength(len, "文字数");
    }

    public static Validator<String> hasLength(int len, String kind){
        return (value, name, em) -> {
            if (value.length() != len) {
                String msg = String.format("%s%sが %d でありません。",
                        nameWith(name, "の"), kind, len);
                em.add(msg);
            }
        };
    }

    public static Validator<String> hasLengthInRange(int lo, int hi) {
        return hasLengthInRange(lo, hi, "文字数");
    }

    public static Validator<String> hasLengthInRange(int lo, int hi, String kind) {
        return (value, name, em) -> {
            int n = value.length();
            if (n < lo) {
                em.add(nameWith(name, "の") + kind + "が少なすぎます。");
            } else if (n > hi) {
                em.add(nameWith(name, "の") + kind + "が多すぎます。");
            }
        };
    }

    public static Validator<String> hasLengthAtMost(int hi, String kind) {
        return (value, name, em) -> {
            int n = value.length();
            if (n > hi) {
                em.add(nameWith(name, "の") + kind + "が多すぎます。");
            }
        };
    }

    public static void isPositive(Integer value, String name, ErrorMessages em) {
        if (!(value > 0)) {
            em.add(nameWith(name, "が") + "正の数値でありません。");
        }
    }

    public static Validator<Integer> is(int expected) {
        return (value, name, em) -> {
            if (value != expected) {
                String msg = String.format("%s値が %d でありません。", nameWith(name, "の"),
                        expected);
                em.add(msg);
            }
        };
    }

    public static Validator<Integer> isNot(int expected) {
        return (value, name, em) -> {
            if (value == expected) {
                String msg = String.format("%s値が %d です。", nameWith(name, "の"),
                        expected);
                em.add(msg);
            }
        };
    }

    public static Validator<Integer> isInRange(int lo, int hi) {
        return (value, name, em) -> {
            if (!(value >= lo && value <= hi)) {
                em.add(nameWith(name, "の") + "値が適切な範囲内でありません。");
            }
        };
    }

    public static Validator<Integer> isEqualOrGreaterThan(int lo) {
        return (value, name, em) -> {
            if (!(value >= lo)) {
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

    public static void isAllDigits(String s, String name, ErrorMessages em){
        if( s == null ){
            return;
        }
        for(int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if( !Character.isDigit(c) ){
                em.add(nameWith(name, "に") + "数字以外の文字が含まれています。");
                return;
            }
        }
    }

    public static Validator<Integer> hasDigitsAtMost(int hi) {
        return (value, name, em) -> {
            if (value < 0) {
                value = -value;
            }
            int ncol = String.format("%d", value).length();
            if (ncol > hi) {
                em.add(nameWith(name, "の") + "桁数が多すぎます。");
            }
        };
    }

    public static Validator<Integer> hasDigitsAtLeast(int lo) {
        return (value, name, em) -> {
            if (value < 0) {
                value = -value;
            }
            int ncol = String.format("%d", value).length();
            if (ncol < lo) {
                em.add(nameWith(name, "の") + "桁数が少なすぎます。");
            }
        };
    }

    public static void hasValidCheckingDigit(Integer value, String name, ErrorMessages em) {
        if (!(HokenLib.hasValidCheckingDigit(value))) {
            em.add(nameWith(name, "の") + "検証番号が正しくありません。");
        }
    }

}
