package jp.chang.myclinic.utilfx.dateinput;

import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.util.kanjidate.GengouNenPair;
import jp.chang.myclinic.util.kanjidate.KanjiDate;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
            GengouNenPair gn = KanjiDate.yearToGengou(value);
            Gengou gengou = gn.gengou;
            setGengou(gengou);
            int nen = gn.nen;
            int month = value.getMonthValue();
            int day = value.getDayOfMonth();
            setNen("" + nen);
            setMonth("" + month);
            setDay("" + day);
        }
    }

    default LocalDate getValue(Consumer<List<String>> errorHandler) {
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
                        int year = KanjiDate.gengouToYear(gengou, nen);
                        value = LocalDate.of(year, month, day);
                        //value = LocalDate.ofEpochDay(JapaneseDate.of(gengou.getEra(), nen, month, day).toEpochDay());
                    } catch (DateTimeException ex) {
                        err.add("日付の入力が不適切です。");
                    }
                }
            }
        } else {
            if (getAllowNull()) {
                value = LocalDate.MAX;
            } else {
                err.add("日付が入力されていません。");
            }
        }
        if (err.size() > 0) {
            if( errorHandler != null ){
                errorHandler.accept(err);
            }
            return null;
        } else {
            return value;
        }
    }

}
