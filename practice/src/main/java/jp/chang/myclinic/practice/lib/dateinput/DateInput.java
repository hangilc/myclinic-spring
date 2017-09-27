package jp.chang.myclinic.practice.lib.dateinput;

import java.time.LocalDate;
import java.util.Optional;

public interface DateInput {
    Optional<LocalDate> getValue();
    boolean isEmpty();
}
