package jp.chang.myclinic.support.meisai;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensa;

import java.time.LocalDate;
import java.util.List;

public interface MeisaiService {
    MeisaiDTO getMeisai(PatientDTO patient, HokenDTO hoken, LocalDate at,
                        List<ShinryouFullDTO> shinryouList, HoukatsuKensa.Revision revision,
                        List<DrugFullDTO> drugs, List<ConductFullDTO> conducts);
}
