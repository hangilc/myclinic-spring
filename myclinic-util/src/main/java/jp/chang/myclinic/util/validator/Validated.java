package jp.chang.myclinic.util.validator;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class Validated<T> {

    private T value;
    private boolean success;
    private List<String> errors;

    public static <U> Validated<U> success(U value) {
        return new Validated<>(value, true, null);
    }

    public static <U> Validated<U> fail(List<String> errors) {
        return new Validated<>(null, false, errors);
    }

    public static <U> Validated<U> fail(String error) {
        return fail(List.of(error));
    }

    private Validated(T value, boolean success, List<String> errors) {
        this.value = value;
        this.success = success;
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getValue() {
        if (!success) {
            throw new RuntimeException("Invalid Validated value access.");
        }
        return value;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getErrorsAsString() {
        return getErrorsAsString(errs -> String.join("\n", errs));
    }

    public String getErrorsAsString(Function<List<String>, String> joiner) {
        return joiner.apply(errors);
    }

    public <U> Validated<U> map(Function<T, U> f) {
        if (isSuccess()) {
            return success(f.apply(getValue()));
        } else {
            return fail(getErrors());
        }
    }

    public <U> Validated<U> flatMap(Function<T, Validated<U>> f) {
        if (isSuccess()) {
            return f.apply(getValue());
        } else {
            return fail(getErrors());
        }
    }

    public <U> Validated<T> extend(String fieldName, Validated<U> fieldValue, BiConsumer<T, U> f) {
        if (fieldValue.isSuccess()) {
            f.accept(getValue(), fieldValue.getValue());
        } else {
            addErrors(fieldName, fieldValue.getErrors());
        }
        return this;
    }

    public interface TriConsumer<P, Q, R> {
        void accept(P p, Q q, R r);
    }

    private void addErrors(String fieldName, List<String> errs) {
        errors.addAll(errs.stream().map(s -> "[" + fieldName + "]" + s).collect(toList()));
    }

    public <U, V> Validated<T> biFlatMap(String fieldName1, Validated<U> fieldValue1,
                                         String fieldName2, Validated<V> fieldValue2,
                                         String unifiedFieldName, BiFunction<U, V, Validated<Void>> biValidator,
                                         TriConsumer<T, U, V> f) {
        if (fieldValue1.isSuccess() && fieldValue2.isSuccess()) {
            Validated<Void> biValidated = biValidator.apply(fieldValue1.getValue(), fieldValue2.getValue());
            if (biValidated.isSuccess()) {
                f.accept(getValue(), fieldValue1.getValue(), fieldValue2.getValue());
            } else {
                addErrors(unifiedFieldName, biValidated.getErrors());
            }
        } else {
            if (!fieldValue1.isSuccess()) {
                addErrors(fieldName1, fieldValue1.getErrors());
            }
            if (!fieldValue2.isSuccess()) {
                addErrors(fieldName2, fieldValue2.getErrors());
            }
        }
        return this;
    }
}
