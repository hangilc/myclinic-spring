package jp.chang.myclinic.util.validator.dto;

import jp.chang.myclinic.dto.ConductShinryouDTO;
import jp.chang.myclinic.util.validator.Validated;

import static jp.chang.myclinic.util.validator.Validated.success;
import static jp.chang.myclinic.util.validator.Validators.*;

public class ConductShinryouValidator {

    private Validated<Integer> validatedConductShinryouId;

    private Validated<Integer> validatedConductId;

    private Validated<Integer> validatedShinryoucode;

    public ConductShinryouValidator validateConductShinryouId(int conductShinryouId) {
        this.validatedConductShinryouId = success(conductShinryouId)
                .confirm(isPositive());
        return this;
    }

    public ConductShinryouValidator setValidatedConductShinryouId(int conductShinryouId) {
        this.validatedConductShinryouId = success(conductShinryouId);
        return this;
    }

    public ConductShinryouValidator validateConductId(int conductId) {
        this.validatedConductId = success(conductId)
                .confirm(isPositive());
        return this;
    }

    public ConductShinryouValidator validateShinryoucode(int shinryoucode) {
        this.validatedShinryoucode = success(shinryoucode).confirm(isPositive());
        return this;
    }

    public Validated<ConductShinryouDTO> validate() {
        return success(new ConductShinryouDTO())
                .extend(
                        "conductShinryouId",
                        validatedConductShinryouId,
                        (dto, conductShinryouId) -> dto.conductShinryouId = conductShinryouId)
                .extend("conductId", validatedConductId, (dto, conductId) -> dto.conductId = conductId)
                .extend(
                        "診療行為コード",
                        validatedShinryoucode,
                        (dto, shinryoucode) -> dto.shinryoucode = shinryoucode);
    }
}
