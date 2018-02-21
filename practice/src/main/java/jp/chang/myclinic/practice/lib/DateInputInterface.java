package jp.chang.myclinic.practice.lib;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

public interface DateInputInterface {

    Gengou getGengou();

    String getNen();

    String getMonth();

    String getDay();

    void setGengou(Gengou gengou);

    void setNen(String nen);

    void setMonth(String month);

    void setDay(String day);

    default boolean getAllowNull() {
        return false;
    }

    default boolean isEmpty() {
        return getNen().isEmpty() && getMonth().isEmpty() && getDay().isEmpty();
    }

    default void setValue(LocalDate value) {
        if (value == null) {
            setNen("");
            setMonth("");
            setDay("");
        } else {
            JapaneseDate jd = JapaneseDate.from(value);
            Gengou gengou = Gengou.fromEra(jd.getEra());
            setGengou(gengou);
            int nen = jd.get(ChronoField.YEAR_OF_ERA);
            int month = value.getMonthValue();
            int day = value.getDayOfMonth();
            setNen("" + nen);
            setMonth("" + month);
            setDay("" + day);
        }
    }

    default Result<LocalDate, List<String>> getValue() {
        List<String> err = new ArrayList<>();
        LocalDate value = null;
        if (!isEmpty()) {
            int nen = 0, month = 0, day = 0;
            try {
                nen = Integer.parseInt(getNen());
            } catch (NumberFormatException ex) {
                err.add("年の入力が不適切です。");
            }
            try {
                month = Integer.parseInt(getMonth());
            } catch (NumberFormatException ex) {
                err.add("月の入力が不適切です。");
            }
            try {
                day = Integer.parseInt(getDay());
            } catch (NumberFormatException ex) {
                err.add("日の入力が不適切です。");
            }
            {
                Gengou gengou = getGengou();
                if (gengou == null) {
                    err.add("元号が設定されていません。");
                } else {
                    try {
                        value = LocalDate.ofEpochDay(JapaneseDate.of(gengou.getEra(), nen, month, day).toEpochDay());
                    } catch (DateTimeException ex) {
                        err.add("日付の入力が不適切です。");
                    }
                }
            }
        } else {
            if (getAllowNull()) {
                value = null;
            } else {
                err.add("日付が入力されていません。");
            }
        }
        if (err.size() > 0) {
            return Result.createError(err);
        } else {
            return Result.createValue(value);
        }
    }

    default void adjust() {
        if (!isEmpty()) {
            Result<LocalDate, List<String>> result = getValue();
            if (!result.hasValue()) {
                int nen = 1, month = 1, day = 1;
                try {
                    nen = Integer.parseInt(getNen());
                    if (nen <= 0) {
                        nen = 1;
                    }
                } catch (NumberFormatException ex) {
                    nen = 1;
                }
                try {
                    month = Integer.parseInt(getMonth());
                    if (month < 1) {
                        month = 1;
                    } else if (month > 12) {
                        month = 12;
                    }
                } catch (NumberFormatException ex) {
                    month = 1;
                }
                try {
                    day = Integer.parseInt(getDay());
                    if (day < 1) {
                        day = 1;
                    }
                    try {
                        LocalDate probe = DateTimeUtil.warekiToLocalDate(getGengou().getEra(), nen, month, 1)
                                .plus(1, ChronoUnit.MONTHS).minus(1, ChronoUnit.DAYS);
                        day = probe.getDayOfMonth();
                    } catch (DateTimeException ex) {
                        day = 1;
                    }
                } catch (NumberFormatException ex) {
                    day = 1;
                }
                setNen("" + nen);
                setMonth("" + month);
                setDay("" + day);
            }
        }
    }

    default void advanceWeek(int n){
        advance(n, ChronoUnit.WEEKS);
    }

    default void advanceDay(int n){
        advance(n, ChronoUnit.DAYS);
    }

    default void advanceMonth(int n){
        advance(n, ChronoUnit.MONTHS);
    }

    default void advanceYear(int n){
        advance(n, ChronoUnit.YEARS);
    }

    default void moveToEndOfMonth(){
        adjust();
        getValue()
                .ifPresent(date -> {
                    LocalDate newDate = date.with(ChronoField.DAY_OF_MONTH, 1).plus(1, ChronoUnit.MONTHS)
                            .minus(1, ChronoUnit.DAYS);
                    setValue(newDate);
                });
    }

    default void moveToEndOfLastMonth(){
        adjust();
        getValue()
                .ifPresent(date -> {
                    LocalDate newDate = LocalDate.now()
                            .with(ChronoField.DAY_OF_MONTH, 1)
                            .minus(1, ChronoUnit.DAYS);
                    setValue(newDate);
                });
    }

    default void advance(int n, TemporalUnit unit){
        adjust();
        getValue()
                .ifPresent(date -> {
                    LocalDate probe = date.plus(n, unit);
                    setValue(probe);
                });
    }

}
