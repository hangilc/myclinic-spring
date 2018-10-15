package jp.chang.myclinic.reception.javafx.edit_hoken;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.logic.*;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static jp.chang.myclinic.util.logic.BiValidators.validInterval;
import static jp.chang.myclinic.util.logic.Converters.*;
import static jp.chang.myclinic.util.logic.Validators.*;
import static jp.chang.myclinic.utilfx.dateinput.DateFormLogic.*;

public class ShahokokuhoFormLogic {

    public static int validateHokenshaBangou(Logic<Integer> source, ErrorMessages em) {
        return source
                .validate(isNotNull())
                .validate(isPositive())
                .validate(hasDigitsInRange(5, 8))
                .validate(isValidHokenshaBangou())
                .getValueOrElse(0, "保険者番号", em);
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

    public static int validateHonnin(Logic<Integer> honnin, ErrorMessages em) {
        return honnin.validate(isNotNull())
                .validate(isOneOf(0, 1))
                .getValueOrElse(0, "本人・家族", em);
    }

    public static Converter<ShahokokuhoFormInputs, ShahokokuhoDTO> shahokokuhoFormInputsToShahokokuhoDTO(){
        return (inputs, name, em) -> {
            ErrorMessages emLocal = new ErrorMessages();
            ShahokokuhoDTO dto = new ShahokokuhoDTO();
            dto.hokenshaBangou = validateHokenshaBangou(new LogicValue<>(inputs.hokenshaBangou)
                    .validate(isNotNull())
                    .validate(isNotEmpty())
                    .convert(stringToInteger()), emLocal);
            validateHihokensha(new LogicValue<>(inputs.hihokenshaKigou), new LogicValue<>(inputs.hihokenshaBangou),
                    (kigou, bangou) -> {
                        dto.hihokenshaKigou = kigou;
                        dto.hihokenshaBangou = bangou;
                    }, emLocal);
            dto.honnin = validateHonnin(new LogicValue<>(inputs.honnin), emLocal);
            new BiLogicValue<>(inputs.validFromInputs, inputs.validUptoInputs)
                    .validate(isNotNull(), valid())
                    .validate(isNotEmptyDateFormInputs(), valid())
                    .convert(dateFormInputsToLocalDate())
                    .validate(validInterval())
                    .convert(localDateToSqldate())
                    .apply(
                            (validFrom, validUpto) -> {
                                dto.validFrom = validFrom;
                                dto.validUpto = validUpto;
                            },
                            "交付年月日", "有効期限", emLocal);
            dto.kourei = new LogicValue<>(inputs.kourei)
                    .validate(isNotNull())
                    .validate(isOneOf(0, 1, 2, 3))
                    .getValueOrElse(0, "高齢", emLocal);
            if (emLocal.hasError()) {
                if( name != null ){
                    em.addComposite(String.format("%sの内容が不適切です。", name), emLocal);
                } else {
                    em.add(emLocal);
                }
                return null;
            }
            return dto;
        };
    }

    public static Converter<ShahokokuhoDTO, ShahokokuhoFormInputs> shahokokuhoDTOToShahokokuhoFormInputs() {
        return (dto, name, em) -> {
            ErrorMessages emLocal = new ErrorMessages();
            ShahokokuhoFormInputs inputs = new ShahokokuhoFormInputs();
            inputs.hokenshaBangou = new LogicValue<>(dto.hokenshaBangou)
                    .convert(zeroOrNullToEmpty())
                    .getValue("保険者番号", emLocal);
            inputs.hihokenshaKigou = dto.hihokenshaKigou;
            inputs.hihokenshaBangou = dto.hihokenshaBangou;
            inputs.honnin = new LogicValue<>(dto.honnin)
                    .validate(isOneOf(0, 1))
                    .getValueOrElse(0, "本人・家族", emLocal);
            inputs.validFromInputs = new LogicValue<>(dto.validFrom)
                    .validate(isNotNull())
                    .convert(sqldateToLocalDate())
                    .validate(isNotNull())
                    .convert(localDateToDateFormInputs())
                    .getValue("交付年月日", emLocal);
            inputs.validUptoInputs = new LogicValue<>(dto.validUpto)
                    .convert(sqldateToLocalDate())
                    .convert(nullableLocalDateToDateFormInputs(Gengou.Current))
                    .getValue("有効期限", emLocal);
            inputs.kourei = dto.kourei;
            if( emLocal.hasError() ){
                if( name != null ){
                    em.addComposite(String.format("%sの内容が不適切です。", name), emLocal);
                } else {
                    em.add(emLocal);
                }
            }
            return inputs; // returns inputs anyway
        };
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
        return (inputs, em) -> {
            return new LogicValue<>(inputs)
                    .convert(shahokokuhoFormInputsToShahokokuhoDTO())
                    .map(dto -> {
                        dto.shahokokuhoId = 0;
                        dto.patientId = patientId;
                        return dto;
                    })
                    .getValue("ShahokokuhoDTO", em);
        };
    }

    public static EnterProc createUpdateProc(ShahokokuhoDTO orig, Consumer<ShahokokuhoFormInputs> formInitializer) {
        ErrorMessages emInputs = new ErrorMessages();
        ShahokokuhoFormInputs initialInputs = new LogicValue<>(orig)
                .convert(shahokokuhoDTOToShahokokuhoFormInputs())
                .getValue("Edit shahokokuho form", emInputs);
        if( emInputs.hasError() ){
            GuiUtil.alertError(emInputs.getMessage());
        }
        formInitializer.accept(initialInputs);
        return (inputs, em) -> new LogicValue<>(inputs)
                .convert(shahokokuhoFormInputsToShahokokuhoDTO())
                .map(dto -> {
                    dto.shahokokuhoId = orig.shahokokuhoId;
                    dto.patientId = orig.patientId;
                    return dto;
                })
                .getValue("ShahokokuhoDTO", em);
    }

}
