package jp.chang.myclinic.reception.javafx.edit_koukikourei;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.KoukikoureiDTO;
import jp.chang.myclinic.util.logic.*;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;
import jp.chang.myclinic.utilfx.dateinput.DateFormLogic;

import java.util.function.Consumer;

class KoukikoureiFormLogic extends LogicUtil {

    //private static Logger logger = LoggerFactory.getLogger(KoukikoureiFormLogic.class);

    private KoukikoureiFormLogic() {

    }

    public static String inputToHokenshaBangou(String input, String name, ErrorMessages em) {
        return new LogicValue<>(input)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .convert(Converters::stringToInteger)
                .validate(Validators.hasDigitsInRange(8, 8))
                .validate(KoukikoureiFormLogic::verifyHouseiBangou)
                .validate(Validators::hasValidCheckingDigit)
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

    static KoukikoureiFormInputs koukikoureiDTOToKoukikoureiFormInputs(KoukikoureiDTO dto,
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

    private static void verifyHouseiBangou(Integer value, String name, ErrorMessages em) {
        int housei = value / 1000000;
        if (housei != 39) {
            em.add(nameWith(name, "の") + "法制番号が３９でありません。");
        }
    }

    @FunctionalInterface
    public interface EnterProc {
        KoukikoureiDTO enter(KoukikoureiFormInputs inputs, ErrorMessages em);
    }

    public static EnterProc createEnterProc(int patientId, Consumer<KoukikoureiFormInputs> formInitializer) {
        KoukikoureiFormInputs initInputs = createDefaultInputs();
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
