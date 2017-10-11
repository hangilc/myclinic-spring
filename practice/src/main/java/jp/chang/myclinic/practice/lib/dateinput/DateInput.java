package jp.chang.myclinic.practice.lib.dateinput;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface DateInput {
    boolean apply(Consumer<LocalDate> valueConsumer, Consumer<List<String>> errorConsumer);
    default <U> U map(Function<LocalDate, U> valueMapper, Function<List<String>, U> errorConsumer){
        Object[] store = new Object[]{ null };
        List<String> errors = new ArrayList<>();
        if( apply(v -> { store[0] = v; }, errors::addAll) ){
            return valueMapper.apply((LocalDate)store[0]);
        } else {
            return errorConsumer.apply(errors);
        }
    }
    //Optional<LocalDate> getValue();
    boolean isEmpty();
}
