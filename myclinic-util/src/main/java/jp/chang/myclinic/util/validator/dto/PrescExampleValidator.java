package jp.chang.myclinic.util.validator.dto;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.util.validator.Validated;

import static java.util.stream.Collectors.toList;
import static jp.chang.myclinic.util.validator.Validated.success;
import static jp.chang.myclinic.util.validator.Validators.*;

import jp.chang.myclinic.dto.PrescExampleDTO;

import java.util.Arrays;
import java.util.List;

public class PrescExampleValidator {

    private Validated<Integer> validatedPrescExampleId;

    private Validated<Integer> validatedIyakuhincode;

    private Validated<String> validatedMasterValidFrom;

    private Validated<String> validatedAmount;

    private Validated<String> validatedUsage;

    private Validated<Integer> validatedDays;

    private Validated<Integer> validatedCategory;

    private Validated<String> validatedComment;

    public PrescExampleValidator validatePrescExampleId(int prescExampleId) {
        this.validatedPrescExampleId = success(prescExampleId).confirm(isPositive());
        return this;
    }

    public PrescExampleValidator setValidatedPrescExampleId(int prescExampleId) {
        this.validatedPrescExampleId = success(prescExampleId);
        return this;
    }

    public PrescExampleValidator validateIyakuhincode(int iyakuhincode) {
        this.validatedIyakuhincode = success(iyakuhincode).confirm(isPositive());
        return this;
    }

    public PrescExampleValidator validateMasterValidFrom(String masterValidFrom) {
        this.validatedMasterValidFrom = success(masterValidFrom).confirm(isValidSqldate());
        return this;
    }

    public PrescExampleValidator validateAmount(String amount) {
        Validated<Double> value = success(amount)
                .confirm(isNotNull())
                .confirm(isNotEmpty())
                .convert(toDouble())
                .confirm(isPositiveDouble());
        this.validatedAmount = Validated.create(amount, value.getErrors());
        return this;
    }

    public PrescExampleValidator validateUsage(String usage) {
        this.validatedUsage = success(usage)
                .confirm(isNotNull())
                .confirm(isNotEmpty());
        return this;
    }

    private PrescExampleValidator validateDays(Validated<Integer> input){
        this.validatedDays = input.confirm(isPositive());
        return this;
    }

    public PrescExampleValidator validateDays(int days) {
        return validateDays(success(days));
    }

    public PrescExampleValidator validateDays(String days) {
        return validateDays(success(days).confirm(isNotNull()).confirm(isNotEmpty()).convert(toInt()));
    }

    public PrescExampleValidator validateCategory(int category) {
        List<Integer> values = Arrays.stream(DrugCategory.values()).map(DrugCategory::getCode)
                .collect(toList());
        this.validatedCategory = success(category).confirm(isOneOf(values));
        return this;
    }

    public PrescExampleValidator validateComment(String comment) {
        this.validatedComment = success(comment);
        return this;
    }

    public Validated<PrescExampleDTO> validate() {
        return success(new PrescExampleDTO())
                .extend(
                        "prescExampleId",
                        validatedPrescExampleId,
                        (dto, prescExampleId) -> dto.prescExampleId = prescExampleId)
                .extend(
                        "医薬品コード",
                        validatedIyakuhincode,
                        (dto, iyakuhincode) -> dto.iyakuhincode = iyakuhincode)
                .extend(
                        "masterValidFrom",
                        validatedMasterValidFrom,
                        (dto, masterValidFrom) -> dto.masterValidFrom = masterValidFrom)
                .extend("用量", validatedAmount, (dto, amount) -> dto.amount = amount)
                .extend("用法", validatedUsage, (dto, usage) -> dto.usage = usage)
                .extend("日数・回数", validatedDays, (dto, days) -> dto.days = days)
                .extend("category", validatedCategory, (dto, category) -> dto.category = category)
                .extend("注釈", validatedComment, (dto, comment) -> dto.comment = comment);
    }
}
