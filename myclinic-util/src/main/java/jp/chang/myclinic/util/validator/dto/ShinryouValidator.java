package jp.chang.myclinic.util.validator.dto;

import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.util.validator.Validated;

import static jp.chang.myclinic.util.validator.Validated.success;
import static jp.chang.myclinic.util.validator.Validators.*;

public class ShinryouValidator {

    private Validated<Integer> validatedShinryouId;

    private Validated<Integer> validatedVisitId;

    private Validated<Integer> validatedShinryoucode;

    public ShinryouValidator validateShinryouId(int shinryouId) {
        this.validatedShinryouId = success(shinryouId)
                .confirm(isPositive());
        return this;
    }

    public ShinryouValidator setValidatedShinryouId(int shinryouId){
        this.validatedShinryouId = success(shinryouId);
        return this;
    }

    public ShinryouValidator validateVisitId(int visitId) {
        this.validatedVisitId = success(visitId)
                .confirm(isPositive());
        return this;
    }

    public ShinryouValidator validateShinryoucode(int shinryoucode) {
        this.validatedShinryoucode = success(shinryoucode)
                .confirm(isPositive());
        return this;
    }

    public Validated<ShinryouDTO> validate() {
        return success(new ShinryouDTO())
                .extend("shinryouId", validatedShinryouId, (dto, shinryouId) -> dto.shinryouId = shinryouId)
                .extend("visitId", validatedVisitId, (dto, visitId) -> dto.visitId = visitId)
                .extend("shinryoucode", validatedShinryoucode, (dto, shinryoucode) -> dto.shinryoucode = shinryoucode);
    }
}

