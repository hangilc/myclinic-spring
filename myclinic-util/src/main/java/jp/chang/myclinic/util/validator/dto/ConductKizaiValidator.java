package jp.chang.myclinic.util.validator.dto;

import jp.chang.myclinic.util.validator.Validated;

import static jp.chang.myclinic.util.validator.Validated.success;
import static jp.chang.myclinic.util.validator.Validators.*;

import jp.chang.myclinic.dto.ConductKizaiDTO;

public class ConductKizaiValidator {

    private Validated<Integer> validatedConductKizaiId;

    private Validated<Integer> validatedConductId;

    private Validated<Integer> validatedKizaicode;

    private Validated<Double> validatedAmount;

    public ConductKizaiValidator validateConductKizaiId(int conductKizaiId) {
        this.validatedConductKizaiId = success(conductKizaiId).confirm(isPositive());
        return this;
    }

    public ConductKizaiValidator setValidatedConductKizaiId(int conductKizaiId) {
        this.validatedConductKizaiId = success(conductKizaiId);
        return this;
    }

    public ConductKizaiValidator validateConductId(int conductId) {
        this.validatedConductId = success(conductId).confirm(isPositive());
        return this;
    }

    public ConductKizaiValidator validateKizaicode(int kizaicode) {
        this.validatedKizaicode = success(kizaicode).confirm(isPositive());
        return this;
    }

    public ConductKizaiValidator validateAmount(String amount) {
        this.validatedAmount = success(amount)
            .apply(validateToDouble())
            .confirm(isPositiveDouble());
        return this;
    }

    public Validated<ConductKizaiDTO> validate() {
        return success(new ConductKizaiDTO())
                .extend(
                        "conductKizaiId",
                        validatedConductKizaiId,
                        (dto, conductKizaiId) -> dto.conductKizaiId = conductKizaiId)
                .extend("conductId", validatedConductId, (dto, conductId) -> dto.conductId = conductId)
                .extend("特定器材コード", validatedKizaicode, (dto, kizaicode) -> dto.kizaicode = kizaicode)
                .extend("用量", validatedAmount, (dto, amount) -> dto.amount = amount);
    }
}
