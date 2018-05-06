package jp.chang.myclinic.rcpt.check;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.mastermap.ResolvedShinryouByoumei;
import jp.chang.myclinic.mastermap.generated.ResolvedDiseaseMap;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.Common;
import okhttp3.mockwebserver.MockWebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Base {

    private static Logger logger = LoggerFactory.getLogger(Base.class);
    ResolvedShinryouMap shinryouMap;
    ResolvedDiseaseMap byoumeiMap;
    Map<Integer, List<ResolvedShinryouByoumei>> shinryouByoumei;
    Common.MasterMaps masterMaps;

    Base() {
        masterMaps = TestListener.masterMaps;
        shinryouMap = TestListener.shinryouMap;
        byoumeiMap = TestListener.masterMaps.resolvedMap.diseaseMap;
        shinryouByoumei = TestListener.masterMaps.shinryouByoumeiMap;
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

    ObjectMapper getObjectMapper(){
        return TestListener.objectMapper;
    }

    <T> T fromJson(String s, Class<T> cls){
        try {
            return getObjectMapper().readValue(s, cls);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    LocalDate getAt(){
        return TestListener.at;
    }
}
