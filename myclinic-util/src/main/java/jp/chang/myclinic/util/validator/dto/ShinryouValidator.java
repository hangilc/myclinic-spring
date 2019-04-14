package jp.chang.myclinic.util.validator.dto;

import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.util.validator.Validated;

import static jp.chang.myclinic.util.validator.Validated.success;
import static jp.chang.myclinic.util.validator.Validators.*;

public class ShinryouValidator {

    private Validated<Integer> validatedShinryouId;

    private Validated<Integer> validatedVisitId;

    private Validated<Integer> validatedShinryoucode;

    public void validateShinryouId(int shinryouId) {
        this.validatedShinryouId = success(shinryouId)
                .confirm(isPositive());
    }

    public void validateVisitId(int visitId) {
        this.validatedVisitId = success(visitId)
                .confirm(isPositive());
    }

    public void validateShinryoucode(int shinryoucode) {
        this.validatedShinryoucode = success(shinryoucode)
                .confirm(isPositive());
    }

    public Validated<ShinryouDTO> validate() {
        return success(new ShinryouDTO())
                .extend("shinryouId", validatedShinryouId, (dto, shinryouId) -> dto.shinryouId = shinryouId)
                .extend("visitId", validatedVisitId, (dto, visitId) -> dto.visitId = visitId)
                .extend("shinryoucode", validatedShinryoucode, (dto, shinryoucode) -> dto.shinryoucode = shinryoucode);
    }
}

