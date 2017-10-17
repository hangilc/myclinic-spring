package jp.chang.myclinic.practice.lib.dateinput;

import jp.chang.myclinic.practice.lib.Result;

import java.time.LocalDate;
import java.util.List;

public interface DateInput {
    Result<LocalDate, List<String>> getValue();
    boolean isEmpty();
}
