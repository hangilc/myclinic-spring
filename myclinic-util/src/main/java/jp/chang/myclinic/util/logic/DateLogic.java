package jp.chang.myclinic.util.logic;

import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.util.kanjidate.KanjiDate;

import java.time.DateTimeException;
import java.time.LocalDate;

import static jp.chang.myclinic.util.logic.Validators.isEqualOrGreaterThan;
import static jp.chang.myclinic.util.logic.Validators.isInRange;

public class DateLogic extends LogicUtil {

    public static LocalDate toLocalDate(String name, Logic<Gengou> gengouLogic, Logic<Integer> nenLogic,
                                     Logic<Integer> monthLogic, Logic<Integer> dayLogic,
                                     ErrorMessages em) {
        name = nameWith(name, "の");
        int ne = em.getNumberOfErrors();
        Gengou gengou = gengouLogic
                .validate(Validators::isNotNull)
                .getValue(name + "元号", em);
        Integer nen = nenLogic
                .validate(Validators::isNotNull)
                .validate(isEqualOrGreaterThan(1))
                .getValue(name + "年", em);
        Integer month = monthLogic
                .validate(Validators::isNotNull)
                .validate(isInRange(1, 12))
                .getValue(name + "月", em);
        Integer day = dayLogic
                .validate(Validators::isNotNull)
                .validate(isInRange(1, 31))
                .getValue(name + "日", em);
        if (em.hasErrorSince(ne)) {
            return null;
        } else {
            try {
                int year = KanjiDate.gengouToYear(gengou, nen);
                return LocalDate.of(year, month, day);
                //return LocalDate.ofEpochDay(JapaneseDate.of(gengou.getEra(), nen, month, day).toEpochDay());
            } catch (DateTimeException ex) {
                em.add(name + "日付が不適切です。");
                return null;
            }
        }
    }

}
