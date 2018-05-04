package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.mastermap.MasterMap;
import jp.chang.myclinic.mastermap.ResolvedMap;
import jp.chang.myclinic.mastermap.ResolvedShinryouByoumei;

import java.util.List;
import java.util.Map;

class Scope {

    List<VisitFull2DTO> visits;
    ResolvedMap resolvedMasterMap;
    Map<Integer, List<ResolvedShinryouByoumei>> shinryouByoumeiMap;
    List<DiseaseFullDTO> diseases;

    Scope(List<VisitFull2DTO> visits, ResolvedMap resolvedMasterMap,
          Map<Integer, List<ResolvedShinryouByoumei>> shinryouByoumeiMap, List<DiseaseFullDTO> diseases) {
        this.visits = visits;
        this.resolvedMasterMap = resolvedMasterMap;
        this.shinryouByoumeiMap = shinryouByoumeiMap;
        this.diseases = diseases;
    }

}
