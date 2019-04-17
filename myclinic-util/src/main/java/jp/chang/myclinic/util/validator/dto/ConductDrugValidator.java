package jp.chang.myclinic.util.validator.dto;

import jp.chang.myclinic.dto.ConductDrugDTO;
import jp.chang.myclinic.util.validator.Validated;

import static jp.chang.myclinic.util.validator.Validated.success;
import static jp.chang.myclinic.util.validator.Validators.*;

public class ConductDrugValidator {

    private Validated<Integer> validatedConductDrugId;

    private Validated<Integer> validatedConductId;

    private Validated<Integer> validatedIyakuhincode;

    private Validated<Double> validatedAmount;

    public ConductDrugValidator validateConductDrugId(int conductDrugId) {
        this.validatedConductDrugId = success(conductDrugId)
                .confirm(isPositive());
        return this;
    }

    public ConductDrugValidator setValidatedConductDrugId(int conductDrugId) {
        this.validatedConductDrugId = success(conductDrugId);
        return this;
    }

    public ConductDrugValidator validateConductId(int conductId) {
        this.validatedConductId = success(conductId)
                .confirm(isPositive());
        return this;
    }

    public ConductDrugValidator setValidateConductId(int conductId) {
        this.validatedConductId = success(conductId);
        return this;
    }

    public ConductDrugValidator validateIyakuhincode(int iyakuhincode) {
        this.validatedIyakuhincode = success(iyakuhincode)
                .confirm(isPositive());
        return this;
    }

    public ConductDrugValidator validateAmount(double amount) {
        this.validatedAmount = success(amount)
                .confirm(isPositiveDouble());
        return this;
    }

    public Validated<ConductDrugDTO> validate() {
        return success(new ConductDrugDTO())
                .extend(
                        "conductDrugId",
                        validatedConductDrugId,
                        (dto, conductDrugId) -> dto.conductDrugId = conductDrugId)
                .extend("conductId", validatedConductId, (dto, conductId) -> dto.conductId = conductId)
                .extend(
                        "医薬品コード",
                        validatedIyakuhincode,
                        (dto, iyakuhincode) -> dto.iyakuhincode = iyakuhincode)
                .extend("用量", validatedAmount, (dto, amount) -> dto.amount = amount);
    }
}

