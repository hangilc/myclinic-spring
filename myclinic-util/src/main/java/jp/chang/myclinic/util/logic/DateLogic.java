package jp.chang.myclinic.util.logic;

import jp.chang.myclinic.consts.Gengou;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.util.List;

import static jp.chang.myclinic.util.logic.Converters.*;
import static jp.chang.myclinic.util.logic.Validators.*;

public class DateLogic implements Logic<LocalDate> {

    public enum Parts {
        GengouPart, NenPart, MonthPart, DayPart, General
    }

    //private static Logger logger = LoggerFactory.getLogger(DateLogic.class);

    private Logic<Gengou> gengouLogic;
    private Logic<Integer> nenLogic;
    private Logic<Integer> monthLogic;
    private Logic<Integer> dayLogic;

    public DateLogic(Logic<Gengou> gengouLogic, Logic<Integer> nenLogic,
                     Logic<Integer> monthLogic, Logic<Integer> dayLogic) {
        if( name == null ){
            this.name = "";
        } else {
            this.name = name + "の";
        }
        this.gengouLogic = gengouLogic;
        this.nenLogic = nenLogic;
        this.monthLogic = monthLogic;
        this.dayLogic = dayLogic;
    }

    @Override
    public LocalDate getValue(String name, ErrorMessages em) {
        if( name != null ){
            name += "の";
        } else {
            name = "";
        }
        int ne = em.getNumberOfErrors();
        Gengou gengou = gengouLogic
                .validate(isNotNull())
                .getValue(name + "元号", em);
        Integer nen = nenLogic
                .validate(isNotNull())
                .validate(isInRange(1, Integer.MAX_VALUE))
                .getValue(name + "年", em);
        Integer month = monthLogic
                .validate(isNotNull())
                .validate(isInRange(1, 12))
                .getValue(name + "月", em);
        Integer day = dayLogic
                .validate(isNotNull())
                .validate(isInRange(1, 31))
                .getValue(name + "日", em);
        if (em.hasError()) {
            return null;
        } else {
            try {
                return LocalDate.ofEpochDay(JapaneseDate.of(gengou.getEra(), nen, month, day).toEpochDay());
            } catch (DateTimeException ex) {
                em.add(name + "日付が不適切です。");
                return null;
            }
        }
    }

}
