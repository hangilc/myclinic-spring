package jp.chang.myclinic.util.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class Validated<T> {

    private T value;
    private List<String> errors;

    public static <U> Validated<U> success(U value) {
        return new Validated<>(value, new ArrayList<>());
    }

    public static <U> Validated<U> fail(String errorMessage) {
        List<String> errors = new ArrayList<>();
        errors.add(errorMessage);
        return fail(errors);
    }

    public static <U> Validated<U> fail(List<String> errors) {
        return new Validated<>(null, errors);
    }

    public static <U> Validated<U> create(U value, List<String> errors) {
        if (errors.size() > 0) {
            value = null;
        }
        return new Validated<>(value, errors);
    }

    private Validated(T value, List<String> errors) {
        this.value = value;
        this.errors = errors;
    }

    public boolean isSuccess() {
        return errors.size() == 0;
    }

    public boolean isFailure() {
        return !isSuccess();
    }

    public T getValue() {
        if (!isSuccess()) {
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

    public Validated<T> confirm(Function<T, String> confirmer) {
        if (isSuccess()) {
            String err = confirmer.apply(getValue());
            if (err != null) {
                errors.add(err);
            }
        }
        return this;
    }

    public <U> Validated<U> convert(Function<T, Validated<U>> f) {
        if (isSuccess()) {
            return f.apply(getValue());
        } else {
            return fail(getErrors());
        }
    }

    public <U> Validated<U> apply(Function<Validated<T>, Validated<U>> f) {
        return f.apply(this);
    }

    public <U> Validated<T> extend(String fieldName, Validated<U> fieldValue, BiConsumer<T, U> f) {
        if (fieldValue == null) {
            throw new RuntimeException("Null validated field.");
        }
        if (value == null) {
            throw new RuntimeException("Null validated value.");
        }
        if (fieldValue.isSuccess()) {
            f.accept(value, fieldValue.getValue());
        } else {
            addErrors(fieldName, fieldValue.getErrors());
        }
        return this;
    }

    private void addErrors(String fieldName, List<String> errs) {
        errors.addAll(errs.stream().map(s -> {
            String sep = s.startsWith("[") ? "" : " ";
            return "[" + fieldName + "]" + sep + s;
        }).collect(toList()));
    }


}
