package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.mastermap.ResolvedMap;
import jp.chang.myclinic.mastermap.ResolvedShinryouByoumei;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

class Scope {
    PatientDTO patient;
    List<VisitFull2DTO> visits;
    ResolvedMap resolvedMasterMap;
    Map<Integer, List<ResolvedShinryouByoumei>> shinryouByoumeiMap;
    List<DiseaseFullDTO> diseases;
    Consumer<Error> errorHandler;
    Fixer api;

    Scope(){}

    Scope(PatientDTO patient, List<VisitFull2DTO> visits, ResolvedMap resolvedMasterMap,
          Map<Integer, List<ResolvedShinryouByoumei>> shinryouByoumeiMap, List<DiseaseFullDTO> diseases,
          Consumer<Error> errorHandler, Fixer api) {
        this.patient = patient;
        this.visits = visits;
        this.resolvedMasterMap = resolvedMasterMap;
        this.shinryouByoumeiMap = shinryouByoumeiMap;
        this.diseases = diseases;
        this.errorHandler = errorHandler;
        this.api = api;
    }

    @Override
    public String toString() {
        return "Scope{" +
                "patient=" + patient +
                ", visits=" + visits +
                ", resolvedMasterMap=" + resolvedMasterMap +
                ", shinryouByoumeiMap=" + shinryouByoumeiMap +
                ", diseases=" + diseases +
                ", errorHandler=" + errorHandler +
                ", api=" + api +
                '}';
    }
}
