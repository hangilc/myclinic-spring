package jp.chang.myclinic.utilfx.dateinput;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.value.ErrorMessages;
import jp.chang.myclinic.util.value.LogicValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.temporal.ChronoField;
import java.util.function.BiConsumer;

import static jp.chang.myclinic.util.value.Converters.nullToZero;
import static jp.chang.myclinic.util.value.Converters.stringToInteger;
import static jp.chang.myclinic.util.value.Validators.*;

public class DateFormLogic {

    private static Logger logger = LoggerFactory.getLogger(DateFormLogic.class);

    public static LocalDate inputsToDate(DateFormInputs inputs, String name, ErrorMessages em){
        if( inputs == null ){
            em.add(String.format("DateFormInputs is null. (%s)", name));
            return null;
        }
        if( inputs.isEmpty() ){
            return null;
        }
        ErrorMessages emDate = new ErrorMessages();
        Gengou gengou = new LogicValue<Gengou>(inputs.gengou)
                .validate(isNotNull())
                .getValue("元号", emDate);
        int nen = new LogicValue<String>(inputs.nen)
                .validate(isNotNull())
                .validate(isNotEmpty())
                .convert(stringToInteger())
                .validate(inRange(1, Integer.MAX_VALUE))
                .convert(nullToZero())
                .getValue("年", emDate);
        int month = new LogicValue<String>(inputs.nen)
                .validate(isNotNull())
                .validate(isNotEmpty())
                .convert(stringToInteger())
                .validate(inRange(1, 12))
                .convert(nullToZero())
                .getValue("月", emDate);
        int day = new LogicValue<String>(inputs.nen)
                .validate(isNotNull())
                .validate(isNotEmpty())
                .convert(stringToInteger())
                .validate(inRange(1, 31))
                .convert(nullToZero())
                .getValue("日", emDate);
        if( emDate.hasError() ){
            em.add(String.format("%sの内容が不適切です。", name));
            em.indent();
            em.add(emDate);
            em.unindent();
            return null;
        } else {
            try {
                return LocalDate.ofEpochDay(JapaneseDate.of(gengou.getEra(), nen, month, day).toEpochDay());
            } catch (DateTimeException ex) {
                em.add(String.format("%sの内容が不適切です。", name));
                return null;
            }
        }
    }

    public static void verifyValidFromAndValidUpto(LocalDate validFrom, LocalDate validUpto,
                                                      String validFromName, String validUptoName,
                                                      ErrorMessages em){
        if( validFrom == null ){
            em.add(String.format("%sが設定されていません。", validFromName));
            return;
        }
        if( validUpto == null ){
            return;
        }
        if (!validFrom.equals(validUpto) && !validFrom.isBefore(validUpto)) {
            em.add(String.format("%sが%sより前の値です。", validUptoName, validFromName));
        }
    }

    public static DateFormInputs dateToInputs(LocalDate date){
        try {
            JapaneseDate jd = JapaneseDate.from(date);
            Gengou gengou = Gengou.fromEra(jd.getEra());
            DateFormInputs inputs = new DateFormInputs(gengou);
            int nen = jd.get(ChronoField.YEAR_OF_ERA);
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();
            inputs.nen = "" + nen;
            inputs.month = "" + month;
            inputs.day = "" + day;
            return inputs;
        } catch (DateTimeException ex) {
            logger.error("Invalid date. " + date);
            return null;
        } catch(NullPointerException ex){
            logger.error("Null date. ");
            return null;
        }
    }

    public static DateFormInputs storageValueToInputs(String store){
        try {
            if( "0000-00-00".equals(store) ){
                return null;
            }
            LocalDate date = LocalDate.parse(store);
            return dateToInputs(date);
        } catch(NumberFormatException ex){
            logger.error("Invalid date string.", ex);
            return null;
        } catch(NullPointerException ex){
            logger.error("Null pointer.", ex);
            return null;
        }
    }

    public static String dateToStorageValue(LocalDate date, String name, ErrorMessages em){
        if( date == null ){
            em.add(String.format("%sが設定されていません。", name));
            return null;
        }
        return date.toString();
    }

    public static String dateToValidUptoStorageValue(LocalDate date, String name, ErrorMessages em){
        if( date == null ){
            return "0000-00-00";
        } else {
            return dateToStorageValue(date, name, em);
        }
    }

    public static void verifyValidFromAndValidUptoInputs(DateFormInputs validFromInputs,
                                                         DateFormInputs validUptoInputs,
                                                         String validFromName, String validUptoName,
                                                         ErrorMessages em,
                                                         BiConsumer<String, String> handler){
        int ne = em.getNumberOfErrors();
        LocalDate validFrom = inputsToDate(validFromInputs, validFromName, em);
        LocalDate validUpto = inputsToDate(validUptoInputs, validUptoName, em);
        if( em.hasErrorSince(ne) ){
            return;
        }
        verifyValidFromAndValidUpto(validFrom, validUpto, validFromName, validUptoName, em);
        if( em.hasErrorSince(ne) ){
            return;
        }
        String validFromStorage = dateToStorageValue(validFrom, validFromName, em);
        String validUptoStorage = dateToValidUptoStorageValue(validUpto, validUptoName, em);
        if( em.hasErrorSince(ne) ){
            return;
        }
        handler.accept(validFromStorage, validUptoStorage);
    }

}
