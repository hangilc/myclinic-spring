package jp.chang.myclinic.util.value.date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.value.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;

public class DateFormLogic implements Logic<LocalDate> {

    private ObjectPropertyLogic<Gengou> gengouSource = new ObjectPropertyLogic<Gengou>();
    private StringPropertyLogic nenSource = new StringPropertyLogic();
    private StringPropertyLogic monthSource = new StringPropertyLogic();
    private StringPropertyLogic daySource = new StringPropertyLogic();
    private Logic<Gengou> gengouLogic;
    private Logic<Integer> nenLogic;
    private Logic<Integer> monthLogic;
    private Logic<Integer> dayLogic;

    public DateFormLogic() {
        this.gengouLogic = gengouSource.chain(Converters.nonNullConverter());
        nenLogic = nenSource
                .chain(Converters.stringToIntegerConverter())
                .chain(Converters.integerRangeConverter(1, Integer.MAX_VALUE));
        monthLogic = monthSource
                .chain(Converters.stringToIntegerConverter())
                .chain(Converters.integerRangeConverter(1, 12));
        dayLogic = daySource
                .chain(Converters.stringToIntegerConverter())
                .chain(Converters.integerRangeConverter(1, 31));
    }

    public void setGengouSource(ObjectProperty<Gengou> src) {
        gengouSource.setProperty(src);
    }

    public void setNenSource(StringProperty src) {
        nenSource.setProperty(src);
    }

    public void setMonthSource(StringProperty src) {
        monthSource.setProperty(src);
    }

    public void setDaySource(StringProperty src) {
        daySource.setProperty(src);
    }

    @Override
    public LocalDate getValue(String name, ErrorMessages em) {
        ErrorMessages dateErrors = new ErrorMessages();
        Gengou gengou = gengouLogic.getValue("元号", dateErrors);
        Integer nen = nenLogic.getValue("年", dateErrors);
        Integer month = monthLogic.getValue("月", dateErrors);
        Integer day = dayLogic.getValue("日", dateErrors);
        if (dateErrors.hasError()) {
            em.add(String.format("%sの設定が不適切です。", name));
            em.indent();
            em.add(dateErrors);
            em.unindent();
            return null;
        } else {
            try {
                return LocalDate.ofEpochDay(JapaneseDate.of(gengou.getEra(), nen, month, day).toEpochDay());
            } catch (DateTimeException ex) {
                em.add(String.format("%sの入力が不適切です。", name));
                return null;
            }
        }

    }
}
