package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.mastermap.ResolvedMap;

import java.util.List;

class Scope {

    List<VisitFull2DTO> visits;
    ResolvedMap resolvedMasterMap;
    List<DiseaseFullDTO> diseases;

    Scope(List<VisitFull2DTO> visits, ResolvedMap resolvedMasterMap, List<DiseaseFullDTO> diseases) {
        this.visits = visits;
        this.resolvedMasterMap = resolvedMasterMap;
        this.diseases = diseases;
    }

}
