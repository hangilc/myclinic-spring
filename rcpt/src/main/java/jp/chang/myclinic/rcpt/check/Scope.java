package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
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
    boolean fixit;
    Consumer<Error> errorHandler;
    Service.ServerAPI api;

    Scope(){}

    Scope(PatientDTO patient, List<VisitFull2DTO> visits, ResolvedMap resolvedMasterMap,
          Map<Integer, List<ResolvedShinryouByoumei>> shinryouByoumeiMap, List<DiseaseFullDTO> diseases,
          boolean fixit, Consumer<Error> errorHandler, Service.ServerAPI api) {
        this.patient = patient;
        this.visits = visits;
        this.resolvedMasterMap = resolvedMasterMap;
        this.shinryouByoumeiMap = shinryouByoumeiMap;
        this.diseases = diseases;
        this.fixit = fixit;
        this.errorHandler = errorHandler;
        this.api = api;
    }

}
