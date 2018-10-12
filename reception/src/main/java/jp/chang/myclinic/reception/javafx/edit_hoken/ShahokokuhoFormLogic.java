package jp.chang.myclinic.reception.javafx.edit_hoken;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.value.ErrorMessages;
import jp.chang.myclinic.util.value.Logic;
import jp.chang.myclinic.util.value.LogicValue;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;
import jp.chang.myclinic.utilfx.dateinput.DateFormLogic;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static jp.chang.myclinic.util.value.Converters.*;
import static jp.chang.myclinic.util.value.Validators.*;

public class ShahokokuhoFormLogic {

    //private static Logger logger = LoggerFactory.getLogger(ShahokokuhoFormLogic.class);

    public static int validateHokenshaBangou(Logic<Integer> source, ErrorMessages em) {
        return source.validate(isPositive())
                .validate(hasDigitsInRange(5, 8))
                .validate(isValidHokenshaBangou())
                .getValueAsInt("保険者番号", em);
    }

    public static void validateHihokensha(Logic<String> kigou, Logic<String> bangou,
                                          BiConsumer<String, String> handler, ErrorMessages em) {
        int ne = em.getNumberOfErrors();
        String hihokenshaKigou = kigou
                .convert(nullToEmpty())
                .getValue("被保険者記号", em);
        String hihokenshaBangou = bangou
                .convert(nullToEmpty())
                .getValue("被保険者番号", em);
        if (em.hasNoErrorSince(ne)) {
            if (hihokenshaKigou.isEmpty() && hihokenshaBangou.isEmpty()) {
                em.add("被保険者記号と被保険者番号が両方空白です。");
            } else {
                handler.accept(hihokenshaKigou, hihokenshaBangou);
            }
        }
    }

    public static int validateHonnin(Logic<Integer> honnin) {
        return honnin.validate(isNotNull())
                .validate(isOneOf(0, 1))
                .getValueAsInt("本人・家族", em);

    }

    public static ShahokokuhoDTO inputsToDTO(ShahokokuhoFormInputs inputs, ErrorMessages em) {
        ShahokokuhoDTO dto = new ShahokokuhoDTO();
        dto.hokenshaBangou = validateHokenshaBangou(new LogicValue<>(inputs.hokenshaBangou)
                .validate(isNotNull())
                .validate(isNotEmpty())
                .convert(stringToInteger()), em);
        validateHihokensha(new LogicValue<>(inputs.hihokenshaKigou), new LogicValue<>(inputs.hihokenshaBangou),
                (kigou, bangou) -> {
                    dto.hihokenshaKigou = kigou;
                    dto.hihokenshaBangou = bangou;
                }, em);
        dto.honnin = validateHonnin(new LogicValue<>(inputs.honnin));
        DateFormLogic.verifyValidFromAndValidUptoInputs(inputs.validFromInputs, inputs.validUptoInputs,
                "交付年月日", "有効期限", em, (validFrom, validUpto) -> {
                    dto.validFrom = validFrom;
                    dto.validUpto = validUpto;
                });
        dto.kourei = new LogicValue<>(inputs.kourei)
                .validate(isNotNull())
                .validate(isOneOf(0, 1, 2, 3))
                .getValueAsInt("高齢", em);
        if (em.hasError()) {
            return null;
        }
        return dto;
    }

    public static ShahokokuhoFormInputs dtoToInputs(ShahokokuhoDTO dto) {
        ShahokokuhoFormInputs inputs = new ShahokokuhoFormInputs();
        inputs.hokenshaBangou = "" + dto.hokenshaBangou;
        inputs.hihokenshaKigou = dto.hihokenshaKigou;
        inputs.hihokenshaBangou = dto.hihokenshaBangou;
        inputs.honnin = dto.honnin;
        inputs.validFromInputs = DateFormLogic.storageValueToInputs(dto.validFrom);
        inputs.validUptoInputs = DateFormLogic.storageValueToInputs(dto.validUpto);
        inputs.kourei = dto.kourei;
        return inputs;
    }

    @FunctionalInterface
    public static interface EnterProc {
        ShahokokuhoDTO enter(ShahokokuhoFormInputs inputs, ErrorMessages em);
    }

    public static EnterProc createEnterProc(int patientId, Consumer<ShahokokuhoFormInputs> formInitializer) {
        ShahokokuhoFormInputs initInputs = new ShahokokuhoFormInputs();
        initInputs.honnin = 0;
        initInputs.validFromInputs = new DateFormInputs(Gengou.Current);
        initInputs.validUptoInputs = new DateFormInputs(Gengou.Current);
        initInputs.kourei = 0;
        formInitializer.accept(initInputs);
        return (inputs, em) -> {
            int ne = em.getNumberOfErrors();
            ShahokokuhoDTO dto = inputsToDTO(inputs, em);
            if (em.hasErrorSince(ne)) {
                return null;
            }
            dto.shahokokuhoId = 0;
            dto.patientId = patientId;
            return dto;
        };
    }

}
