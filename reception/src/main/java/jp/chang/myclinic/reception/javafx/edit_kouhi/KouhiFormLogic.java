package jp.chang.myclinic.reception.javafx.edit_kouhi;

import jp.chang.myclinic.dto.KouhiDTO;
import jp.chang.myclinic.util.logic.*;
import jp.chang.myclinic.utilfx.dateinput.DateFormLogic;

class KouhiFormLogic extends LogicUtil {

    //private static Logger logger = LoggerFactory.getLogger(KouhiFormLogic.class);

    private KouhiFormLogic() {

    }

    KouhiDTO kouhiFormInputsToKouhiDTO(KouhiFormInputs inputs, String name, ErrorMessages em) {
        int ne = em.getNumberOfErrors();
        KouhiDTO dto = new KouhiDTO();

        dto.futansha = new LogicValue<>(inputs.futanshaBangou)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .convert(Converters::stringToInteger)
                .validate(Validators::isPositive)
                .validate(Validators.hasDigitsInRange(8, 8))
                validate fuken bangou
                .validate(Validators::hasValidCheckingDigit)
                .getValueOrElse(0, nameWith(name, "の") + "負担者番号", em);

        dto.jukyuusha = new LogicValue<>(inputs.jukyuushaBangou)
                .validate(Validators::isNotNull)
                .validate(Validators::isNotEmpty)
                .convert(Converters::stringToInteger)
                .validate(Validators::isPositive)
                .validate(Validators.hasDigitsInRange(8, 8))
                .validate(Validators::hasValidCheckingDigit)
                .getValueOrElse(0, nameWith(name, "の") + "受給者番号", em);

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

        return em.hasErrorSince(ne) ? null : dto;
    }

    KouhiFormInputs kouhiDTOToKouhiFormInputs(KouhiDTO dto, String name, ErrorMessages em){
        int ne = em.getNumberOfErrors();
        KouhiFormInputs inputs = new KouhiFormInputs();

        inputs.futanshaBangou = new LogicValue<>(dto.futansha)
                .map(Mappers::integerToStringWithZeroOrNullToEmpty)
                .getValue(nameWith(name, "の") + "負担者番号", em);

        inputs.jukyuushaBangou = new LogicValue<>(dto.jukyuusha)
                .map(Mappers::integerToStringWithZeroOrNullToEmpty)
                .getValue(nameWith(name, "の") + "受給者番号", em);

        inputs.validFromInputs = new LogicValue<>(dto.validFrom)
                .convert(DateFormLogic::validFromSqldateToDateFormInputs)
                .getValue(nameWith(name, "の") + "資格取得日", em);

        inputs.validUptoInputs = new LogicValue<>(dto.validUpto)
                .convert(DateFormLogic::validUptoSqldateToDateFormInputs)
                .getValue(nameWith(name, "の") + "有効期限", em);

        return inputs; // return inputs anyway
    }

}
