package jp.chang.myclinic.util.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;
import static jp.chang.myclinic.util.validator.Validated.*;

public class Validators {

    public static <U> Function<U, String> confirmer(Function<U, Boolean> pred, String errorMessage) {
        return (U value) -> {
            if (pred.apply(value)) {
                return null;
            } else {
                return errorMessage;
            }
        };
    }

    public static <U> Function<U, String> isNotNull() {
        return confirmer(Objects::nonNull, "Null valud encountered.");
    }

    public static Function<String, String> isNotEmpty() {
        return confirmer(s -> !s.isEmpty(), "空白です。");
    }

    public static Function<Integer, String> isPositive() {
        return confirmer(value -> value > 0, "正の整数でありません。");
    }

    public static Function<Double, String> isPositiveDouble() {
        return confirmer(value -> value > 0, "正の数でありません。");
    }

    public static <T> Function<T, String> isOneOf(List<T> candidates) {
        return confirmer(candidates::contains,
                String.format("適切な値でありません（%s）。",
                        candidates.stream().map(Object::toString).collect(joining(", "))));
    }

    public static <T> Function<T, String> isOneOf(T... candidates) {
        return isOneOf(Arrays.asList(candidates));
    }

    public static Function<String, Validated<Integer>> toInt() {
        return (String input) -> {
            try {
                return Validated.success(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                return Validated.fail("整数に変換できません。");
            }
        };
    }

    public static Function<Validated<String>, Validated<Integer>> validateToPositiveInt() {
        return src -> src.confirm(isNotNull())
                .confirm(isNotEmpty())
                .convert(toInt());
    }

}
