package jp.chang.myclinic.util.validator.dto;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.util.validator.Validated;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;
import static jp.chang.myclinic.util.validator.Validated.*;
import static jp.chang.myclinic.util.validator.Validators.*;

public class DrugValidator {

    private Validated<Integer> validatedDrugId;
    private Validated<Integer> validatedVisitId;
    private Validated<Integer> validatedIyakuhincode;
    private Validated<Double> validatedAmount;
    private Validated<String> validatedUsage;
    private Validated<Integer> validatedDays;
    private Validated<Integer> validatedCategory;
    private Validated<Integer> validatedPrescribed;

    public void validateDrugId(int drugId) {
        this.validatedDrugId = success(drugId)
                .confirm(isPositive());
    }

    public void setValidatedDrugId(Validated<Integer> validatedDrugId) {
        this.validatedDrugId = validatedDrugId;
    }

    public void validateVisitId(int visitId) {
        this.validatedVisitId = success(visitId)
                .confirm(isPositive());
    }

    public void validateIyakuhincode(int iyakuhincode) {
        this.validatedIyakuhincode = success(iyakuhincode)
                .confirm(isPositive());
    }

    private void validateAmount(Validated<Double> validated) {
        this.validatedAmount = validated.confirm(isPositiveDouble());
    }

    public void validateAmount(double amount) {
        validateAmount(success(amount));
    }

    public void validateAmount(String input) {
        validateAmount(success(input)
                .confirm(isNotNull())
                .confirm(isNotEmpty())
                .convert(toDouble())
        );
    }

    public void validateUsage(String usage) {
        this.validatedUsage = success(usage)
                .confirm(isNotNull())
                .confirm(isNotEmpty());
    }

    public void validateDays(String input) {
        this.validatedDays = success(input)
                .apply(validateToPositiveInt());
    }

    public void validateCategory(int category) {
        this.validatedCategory = success(category)
                .confirm(isOneOf(
                        Arrays.stream(DrugCategory.values()).map(DrugCategory::getCode).collect(toList())
                ));
    }

    public void validatePrescribed(int prescribed) {
        this.validatedPrescribed = success(prescribed)
                .confirm(isOneOf(0, 1));
    }

    public Validated<DrugDTO> validate() {
        return success(new DrugDTO())
                .extend("drugId", validatedDrugId, (d, drugId) -> d.drugId = drugId)
                .extend("visitId", validatedVisitId, (d, visitId) -> d.visitId = visitId)
                .extend("iyakuhincode", validatedIyakuhincode, (d, iyakuhincode) -> d.iyakuhincode = iyakuhincode)
                .extend("用量", validatedAmount, (d, amount) -> d.amount = amount)
                .extend("用法", validatedUsage, (d, usage) -> d.usage = usage)
                .extend("日数・回数", validatedDays, (d, days) -> d.days = days)
                .extend("category", validatedCategory, (d, category) -> d.category = category)
                .extend("prescribed", validatedPrescribed, (d, prescribed) -> d.prescribed = prescribed);
    }

}
