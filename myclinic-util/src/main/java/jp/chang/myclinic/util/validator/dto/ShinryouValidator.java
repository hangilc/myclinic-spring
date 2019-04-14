package jp.chang.myclinic.util.validator.dto;

import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.util.validator.Validated;

import static jp.chang.myclinic.util.validator.Validated.success;

public class ShinryouValidator {

    private Validated<Integer> validatedShinryouId;

    private Validated<Integer> validatedVisitId;

    private Validated<Integer> validatedShinryoucode;

    public void validateShinryouId(int shinryouId) {
        this.validatedShinryouId = success(shinryouId);
    }

    public void validateVisitId(int visitId) {
        this.validatedVisitId = success(visitId);
    }

    public void validateShinryoucode(int shinryoucode) {
        this.validatedShinryoucode = success(shinryoucode);
    }

    public Validated<ShinryouDTO> validate() {
        return success(new ShinryouDTO())
                .extend("", validatedShinryouId, (dto, shinryouId) -> dto.shinryouId = shinryouId)
                .extend("", validatedVisitId, (dto, visitId) -> dto.visitId = visitId)
                .extend("", validatedShinryoucode, (dto, shinryoucode) -> dto.shinryoucode = shinryoucode);
    }
}
}
