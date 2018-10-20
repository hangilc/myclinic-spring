package jp.chang.myclinic.reception.javafx.edit_hoken;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.dto_validator.ShahokokuhoLogic;
import jp.chang.myclinic.util.logic.*;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;
import jp.chang.myclinic.utilfx.dateinput.DateFormLogic;

import java.util.function.Consumer;

import static jp.chang.myclinic.util.logic.Validators.hasDigitsInRange;
import static jp.chang.myclinic.util.logic.Validators.isOneOf;

public class ShahokokuhoFormLogic extends LogicUtil {

    public static ShahokokuhoDTO shahokokuhoFormInputsToShahokokuhoDTO(ShahokokuhoFormInputs inputs,
                                                                       String name, ErrorMessages em) {
        int ne = em.getNumberOfErrors();
        ShahokokuhoDTO dto = new ShahokokuhoDTO();

        dto.hokenshaBangou = new LogicValue<>(inputs.hokenshaBangou)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .validate(Validators.hasLengthInRange(5, 8))
                .convert(Converters::stringToInteger)
                .validate(Validators::isPositive)
                .validate(hasDigitsInRange(5, 8))
                .validate(Validators::hasValidCheckingDigit)
                .getValueOrElse(0, nameWith(name, "の") + "保険者番号", em);

        new BiLogicValue<>(inputs.hihokenshaKigou, inputs.hihokenshaBangou)
                .map(Mappers::nullToEmpty)
                .validate(BiValidators::notBothAreEmpty)
                .apply(
                        (kigou, bangou) -> {
                            dto.hihokenshaKigou = kigou;
                            dto.hihokenshaBangou = bangou;
                        },
                        nameWith(name, "の") + "被保険者記号",
                        nameWith(name, "の") + "被保険者番号",
                        em
                );

        dto.honnin = new LogicValue<>(inputs.honnin)
                .validate(Validators.isOneOf(0, 1))
                .getValueOrElse(0, nameWith(name, "の") + "本人・家族", em);

        new BiLogicValue<>(inputs.validFromInputs, inputs.validUptoInputs)
                .convert(DateFormLogic::dateFormValidIntervalToSqldate)
                .apply(
                        (validFrom, validUpto) -> {
                            dto.validFrom = validFrom;
                            dto.validUpto = validUpto;
                        },
                        nameWith(name, "の") + "資格所得日",
                        nameWith(name, "の") + "有効期限",
                        em);

        dto.kourei = new LogicValue<>(inputs.kourei)
                .validate(Validators::isNotNull)
                .validate(isOneOf(0, 1, 2, 3))
                .getValueOrElse(0, nameWith(name, "の") + "高齢", em);

        if (em.hasErrorSince(ne)) {
            return null;
        }
        return dto;
    }

    public static ShahokokuhoFormInputs shahokokuhoDTOToShahokokuhoFormInputs(ShahokokuhoDTO dto,
                                                                              String name, ErrorMessages em) {
        ShahokokuhoFormInputs inputs = new ShahokokuhoFormInputs();
        inputs.hokenshaBangou = new LogicValue<>(dto.hokenshaBangou)
                .map(bangou -> {
                    if( bangou == null || bangou == 0 ){
                        return "";
                    } else {
                        return ShahokokuhoLogic.formatHokenshaBangou(bangou);
                    }
                })
                .getValue(nameWith(name, "の") + "保険者番号", em);
        inputs.hihokenshaKigou = dto.hihokenshaKigou;
        inputs.hihokenshaBangou = dto.hihokenshaBangou;
        inputs.honnin = new LogicValue<>(dto.honnin)
                .validate(isOneOf(0, 1))
                .getValueOrElse(0, nameWith(name, "の") + "本人・家族", em);
        inputs.validFromInputs = new LogicValue<>(dto.validFrom)
                .convert(DateFormLogic::validFromSqldateToDateFormInputs)
                .getValue(nameWith(name, "の") + "資格取得日", em);
        inputs.validUptoInputs = new LogicValue<>(dto.validUpto)
                .convert(DateFormLogic::validUptoSqldateToDateFormInputs)
                .getValue(nameWith(name, "の") + "有効期限", em);
        inputs.kourei = dto.kourei;
        return inputs; // returns inputs anyway
    }

    @FunctionalInterface
    public interface EnterProc {
        ShahokokuhoDTO enter(ShahokokuhoFormInputs inputs, ErrorMessages em);
    }

    public static EnterProc createEnterProc(int patientId, Consumer<ShahokokuhoFormInputs> formInitializer) {
        ShahokokuhoFormInputs initInputs = new ShahokokuhoFormInputs();
        initInputs.honnin = 0;
        initInputs.validFromInputs = new DateFormInputs(Gengou.Current);
        initInputs.validUptoInputs = new DateFormInputs(Gengou.Current);
        initInputs.kourei = 0;
        formInitializer.accept(initInputs);
        return (inputs, em) -> new LogicValue<>(inputs)
                .convert(ShahokokuhoFormLogic::shahokokuhoFormInputsToShahokokuhoDTO)
                .map(dto -> {
                    dto.shahokokuhoId = 0;
                    dto.patientId = patientId;
                    return dto;
                })
                .getValue(null, em);
    }

    public static EnterProc createUpdateProc(ShahokokuhoDTO orig, Consumer<ShahokokuhoFormInputs> formInitializer) {
        ErrorMessages emInputs = new ErrorMessages();
        ShahokokuhoFormInputs initialInputs = new LogicValue<>(orig)
                .convert(ShahokokuhoFormLogic::shahokokuhoDTOToShahokokuhoFormInputs)
                .getValue("Edit shahokokuho form", emInputs);
        if (emInputs.hasError()) {
            GuiUtil.alertError(emInputs.getMessage());
        }
        formInitializer.accept(initialInputs);
        return (inputs, em) -> new LogicValue<>(inputs)
                .convert(ShahokokuhoFormLogic::shahokokuhoFormInputsToShahokokuhoDTO)
                .map(dto -> {
                    dto.shahokokuhoId = orig.shahokokuhoId;
                    dto.patientId = orig.patientId;
                    return dto;
                })
                .getValue(null, em);
    }

}
