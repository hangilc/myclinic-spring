package jp.chang.myclinic.util.dto_validator;

import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.Logic;
import jp.chang.myclinic.util.logic.LogicValue;
import jp.chang.myclinic.util.logic.Validator;

import java.util.function.BiConsumer;

import static jp.chang.myclinic.util.dto_validator.ValidatorLib.checkValidInterval;
import static jp.chang.myclinic.util.logic.Converters.nullToEmpty;
import static jp.chang.myclinic.util.logic.Validators.*;

public class HokenValidator {

    //private static Logger logger = LoggerFactory.getLogger(HokenValidator.class);

    private HokenValidator() {

    }

    public static Logic<Integer> checkShahokokuhoHokenshaBangou(Logic<Integer> src) {
        return src.validate(isNotNull())
                .validate(isPositive())
                .validate(hasDigitsInRange(5, 8))
                .validate(isValidHokenshaBangou());
    }

    public static void checkShahokokuhoHihokensha(Logic<String> kigou, Logic<String> bangou,
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


    public static Validator<ShahokokuhoDTO> validateShahokokuhoBody() {
        return (dto, name, em) -> {
            checkShahokokuhoHokenshaBangou(new LogicValue<>(dto.hokenshaBangou)).verify("保険者番号", em);
            checkShahokokuhoHihokensha(new LogicValue<>(dto.hihokenshaKigou),
                    new LogicValue<>(dto.hihokenshaBangou),
                    (a, b) -> {
                    }, em);
            new LogicValue<>(dto.honnin)
                    .validate(isNotNull())
                    .validate(isOneOf(0, 1))
                    .verify("本人・家族", em);
            checkValidInterval(dto.validFrom, dto.validUpto, "交付年月日", "有効期限", em);
        };
    }

    public static Validator<ShahokokuhoDTO> validateShahokokuhoForEnter() {
        return (dto, name, em) -> {

        }
    }

}
