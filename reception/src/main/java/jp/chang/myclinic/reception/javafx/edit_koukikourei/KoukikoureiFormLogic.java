package jp.chang.myclinic.reception.javafx.edit_koukikourei;

import jp.chang.myclinic.dto.KoukikoureiDTO;
import jp.chang.myclinic.reception.Globals;
import jp.chang.myclinic.util.dto_logic.KoukikoureiLogic;
import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.util.kanjidate.GengouNenPair;
import jp.chang.myclinic.util.kanjidate.KanjiDate;
import jp.chang.myclinic.util.logic.*;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;
import jp.chang.myclinic.utilfx.dateinput.DateFormLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.Consumer;
import java.util.zip.DataFormatException;

class KoukikoureiFormLogic extends LogicUtil {

   private static Logger logger = LoggerFactory.getLogger(KoukikoureiFormLogic.class);

    private KoukikoureiFormLogic() {

    }

    public static String inputToHokenshaBangou(String input, String name, ErrorMessages em) {
        return new LogicValue<>(input)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .convert(Converters::stringToInteger)
                .validate(KoukikoureiLogic::isValidKoukikoureiHokenshaBangou)
                .map(Mappers::integerToString)
                .getValue(nameWith(name, "の") + "保険者番号", em);
    }

    public static String inputToHihokenshaBangou(String input, String name, ErrorMessages em){
        return new LogicValue<>(input)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .validate(Validators.hasLength(8, "桁数"))
                .validate(Validators::isAllDigits)
                .getValue(nameWith(name, "の") + "被保険者番号", em);
    }

    public static KoukikoureiDTO koukikoureiFormInputsToKoukikoureiDTO(KoukikoureiFormInputs inputs,
                                                                       String name, ErrorMessages em) {
        int ne = em.getNumberOfErrors();
        KoukikoureiDTO dto = new KoukikoureiDTO();

        dto.hokenshaBangou = inputToHokenshaBangou(inputs.hokenshaBangou, name, em);

        dto.hihokenshaBangou = inputToHihokenshaBangou(inputs.hihokenshaBangou, name, em);

        new BiLogicValue<>(inputs.validFromInputs, inputs.validUptoInputs)
                .convert(DateFormLogic::dateFormValidIntervalToSqldate)
                .apply(
                        (validFrom, validUpto) -> {
                            dto.validFrom = validFrom;
                            dto.validUpto = validUpto;
                        },
                        nameWith(name, "の") + "資格取得日",
                        nameWith(name, "の") + "有効期限",
                        em);

        dto.futanWari = new LogicValue<>(inputs.futanwari)
                .validate(Validators::isNotNull)
                .validate(Validators.isOneOf(1, 2, 3))
                .getValueOrElse(0, "負担割", em);

        return em.hasErrorSince(ne) ? null : dto;
    }

    private static KoukikoureiFormInputs newKoukikoureiFormInputs(){
        KoukikoureiFormInputs inputs = createDefaultInputs();
        Integer hokenshaBangou = Globals.getAppVars().getDefaultKoukikoureiHokenshaBangou();
        if( hokenshaBangou != null ){
            inputs.hokenshaBangou = hokenshaBangou.toString();
        }
        String validFrom = Globals.getAppVars().getDefaultKoukikoureiValidFrom();
        if( validFrom != null ){
            try {
                LocalDate date = LocalDate.parse(validFrom);
                GengouNenPair gn = KanjiDate.yearToGengou(date);
                inputs.validFromInputs = new DateFormInputs(
                        gn.gengou, gn.nen, date.getMonthValue(), date.getDayOfMonth()
                );
            } catch(DateTimeParseException ex){
                logger.warn("Failed to parse koukikourei valid from. {}", validFrom, ex);
            }
        }
        String validUpto = Globals.getAppVars().getDefaultKoukikoureiValidUpto();
        if( validUpto != null ){
            try {
                LocalDate date = LocalDate.parse(validUpto);
                GengouNenPair gn = KanjiDate.yearToGengou(date);
                inputs.validUptoInputs = new DateFormInputs(
                        gn.gengou, gn.nen, date.getMonthValue(), date.getDayOfMonth()
                );
            } catch(DateTimeParseException ex){
                logger.warn("Failed to parse koukikourei valid upto. {}", validUpto, ex);
            }
        }
        return inputs;
    }

    private static KoukikoureiFormInputs koukikoureiDTOToKoukikoureiFormInputs(KoukikoureiDTO dto,
                                                                       String name, ErrorMessages em) {
        int ne = em.getNumberOfErrors();
        KoukikoureiFormInputs inputs = new KoukikoureiFormInputs();

        inputs.hokenshaBangou = new LogicValue<>(dto.hokenshaBangou)
                .map(Mappers::nullToEmpty)
                .getValue(nameWith(name, "の") + "保険者番号", em);

        inputs.hihokenshaBangou = new LogicValue<>(dto.hihokenshaBangou)
                .map(Mappers::nullToEmpty)
                .getValue(nameWith(name, "の") + "被保険者番号", em);

        inputs.validFromInputs = new LogicValue<>(dto.validFrom)
                .convert(DateFormLogic::validFromSqldateToDateFormInputs)
                .getValue(nameWith(name, "の") + "資格取得日", em);

        inputs.validUptoInputs = new LogicValue<>(dto.validUpto)
                .convert(DateFormLogic::validUptoSqldateToDateFormInputs)
                .getValue(nameWith(name, "の") + "有効期限", em);

        inputs.futanwari = new LogicValue<>(dto.futanWari)
                .validate(Validators::isNotNull)
                .getValueOrElse(0, nameWith(name, "の") + "負担割", em);

        return em.hasErrorSince(ne) ? null : inputs;
    }

    @FunctionalInterface
    public interface EnterProc {
        KoukikoureiDTO enter(KoukikoureiFormInputs inputs, ErrorMessages em);
    }

    public static EnterProc createEnterProc(int patientId, Consumer<KoukikoureiFormInputs> formInitializer) {
        KoukikoureiFormInputs initInputs = newKoukikoureiFormInputs();
        formInitializer.accept(initInputs);
        return (inputs, em) -> new LogicValue<>(inputs)
                .convert(KoukikoureiFormLogic::koukikoureiFormInputsToKoukikoureiDTO)
                .map(dto -> {
                    dto.koukikoureiId = 0;
                    dto.patientId = patientId;
                    return dto;
                })
                .getValue(null, em);
    }

    private static KoukikoureiFormInputs createDefaultInputs() {
        KoukikoureiFormInputs inputs = new KoukikoureiFormInputs();
        inputs.hokenshaBangou = "";
        inputs.hihokenshaBangou = "";
        inputs.validFromInputs = new DateFormInputs(Gengou.Current, "", "", "");
        inputs.validUptoInputs = new DateFormInputs(Gengou.Current, "", "", "");
        inputs.futanwari = 1;
        return inputs;
    }

    public static EnterProc createEditProc(KoukikoureiDTO orig, Consumer<KoukikoureiFormInputs> formInitializer) {
        ErrorMessages emInputs = new ErrorMessages();
        KoukikoureiFormInputs initialInputs = new LogicValue<>(orig)
                .convert(KoukikoureiFormLogic::koukikoureiDTOToKoukikoureiFormInputs)
                .getValue(null, emInputs);
        if (emInputs.hasError()) {
            GuiUtil.alertError(emInputs.getMessage());
        }
        formInitializer.accept(initialInputs);
        return (inputs, em) -> new LogicValue<>(inputs)
                .convert(KoukikoureiFormLogic::koukikoureiFormInputsToKoukikoureiDTO)
                .map(dto -> {
                    dto.koukikoureiId = orig.koukikoureiId;
                    dto.patientId = orig.patientId;
                    return dto;
                })
                .getValue(null, em);
    }


}
