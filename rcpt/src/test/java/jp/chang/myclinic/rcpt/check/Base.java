package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.mastermap.generated.ResolvedDiseaseMap;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.Common;
import okhttp3.mockwebserver.MockWebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

class Base {

    private static Logger logger = LoggerFactory.getLogger(Base.class);
    ResolvedShinryouMap shinryouMap;
    ResolvedDiseaseMap byoumeiMap;
    Common.MasterMaps masterMaps;
    int nerror;

    Base() {
        masterMaps = TestListener.masterMaps;
        shinryouMap = TestListener.shinryouMap;
        byoumeiMap = TestListener.masterMaps.resolvedMap.diseaseMap;
    }

    Scope createScope() {
        Scope scope = new Scope();
        scope.visits = new ArrayList<>();
        scope.patient = new PatientDTO();
        scope.resolvedMasterMap = masterMaps.resolvedMap;
        scope.shinryouByoumeiMap = masterMaps.shinryouByoumeiMap;
        scope.diseases = new ArrayList<>();
        scope.api = Service.api;
        return scope;
    }

    MockWebServer getServer(){
        return TestListener.server;
    }

}
