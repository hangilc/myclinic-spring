package jp.chang.myclinic.reception.javafx.edit_hoken;

import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.value.ErrorMessages;
import jp.chang.myclinic.util.value.LogicValue;
import jp.chang.myclinic.utilfx.dateinput.DateFormLogic;

import static jp.chang.myclinic.util.value.Validators.*;
import static jp.chang.myclinic.util.value.Converters.*;

public class ShahokokuhoFormLogic {

    //private static Logger logger = LoggerFactory.getLogger(ShahokokuhoFormLogic.class);

    public static ShahokokuhoDTO inputsToDTO(ShahokokuhoFormInputs inputs) {
        ErrorMessages em = new ErrorMessages();
        ShahokokuhoDTO dto = new ShahokokuhoDTO();
        dto.hokenshaBangou = new LogicValue<>(inputs.hokenshaBangou)
                .validate(isNotNull())
                .validate(isNotEmpty())
                .convert(stringToInteger())
                .validate(isPositive())
                .validate(isValidHokenshaBangou())
                .getValue("保険者番号", em);
        int ne = em.getNumberOfErrors();
        dto.hihokenshaKigou = new LogicValue<>(inputs.hihokenshaKigou)
                .convert(nullToEmpty())
                .getValue("被保険者記号", em);
        dto.hihokenshaBangou = new LogicValue<>(inputs.hihokenshaBangou)
                .convert(nullToEmpty())
                .getValue("被保険者番号", em);
        if (em.hasNoErrorSince(ne)) {
            if (dto.hihokenshaKigou.isEmpty() && dto.hihokenshaBangou.isEmpty()) {
                em.add("被保険者記号と被保険者番号が両方空白です。");
            }
        }
        dto.honnin = new LogicValue<>(inputs.honnin)
                .validate(isNotNull())
                .validate(isOneOf(0, 1))
                .getValue("本人・家族", em);
        DateFormLogic.verifyValidFromAndValidUptoInputs(inputs.validFromInputs, inputs.validUptoInputs,
                "交付年月日", "有効期限", em, (validFrom, validUpto) -> {
                    dto.validFrom = validFrom;
                    dto.validUpto = validUpto;
                });
        dto.kourei = new LogicValue<>(inputs.kourei)
                .validate(isNotNull())
                .validate(isOneOf(0, 1, 2, 3))
                .getValue("高齢", em);
        if( em.hasError() ){
            return null;
        }
        return dto;
    }

    public static ShahokokuhoFormInputs dtoToInputs(ShahokokuhoDTO dto){
        ShahokokuhoFormInputs inputs = new ShahokokuhoFormInputs();
        inputs.hokenshaBangou = "" + dto.hokenshaBangou;
        inputs.hihokenshaKigou = dto.hihokenshaKigou;
        inputs.hihokenshaBangou = dto.hihokenshaBangou;
        inputs.honnin = dto.honnin;
        inputs.validFromInputs =
    }

}
