package jp.chang.myclinic.rcpt.check;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShinryouDTO;
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

import static org.junit.Assert.assertEquals;

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

    Scope createScope(FixerLog log) {
        Scope scope = new Scope();
        scope.visits = new ArrayList<>();
        scope.patient = new PatientDTO();
        scope.resolvedMasterMap = masterMaps.resolvedMap;
        scope.shinryouByoumeiMap = masterMaps.shinryouByoumeiMap;
        scope.diseases = new ArrayList<>();
        scope.api = new FixerMock(log);
        return scope;
    }

    MockWebServer getServer() {
        return TestListener.server;
    }

    ObjectMapper getObjectMapper() {
        return TestListener.objectMapper;
    }

    <T> T fromJson(String s, Class<T> cls) {
        try {
            return getObjectMapper().readValue(s, cls);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    LocalDate getAt() {
        return TestListener.at;
    }

    void assertBatchDeleteShinryou(int shinryouId, FixerLog log){
        assertEquals(1, log.getBatchDeletedShinryouList().size());
        assertEquals(shinryouId, (int)log.getBatchDeletedShinryouList().get(0).get(0));
    }

    void assertEnterShinryou(int visitId, int shinryoucode, FixerLog log){
        List<ShinryouDTO> enteredList = log.getEnteredShinryouList();
        assertEquals(1, enteredList.size());
        ShinryouDTO entered = enteredList.get(0);
        ShinryouDTO expected = new ShinryouDTO();
        expected.shinryouId = 0;
        expected.shinryoucode = shinryoucode;
        entered.visitId = visitId;
        assertEquals(expected, entered);
    }

//    void assertBatchDeleteShinryou(int shinryouId, RecordedRequest req) {
//        HttpUrl httpUrl = req.getRequestUrl();
//        assertEquals("POST", req.getMethod());
//        assertEquals("/batch-delete-shinryou", httpUrl.encodedPath());
//        assertEquals(Set.of("shinryou-id"), httpUrl.queryParameterNames());
//        assertEquals("" + shinryouId, httpUrl.queryParameter("shinryou-id"));
//    }
//
//    void assertBatchDeleteShinryou(Set<Integer> shinryouIds, RecordedRequest req) {
//        assertEquals("POST", req.getMethod());
//        HttpUrl httpUrl = req.getRequestUrl();
//        assertEquals("/batch-delete-shinryou", httpUrl.encodedPath());
//        assertEquals(Set.of("shinryou-id"), httpUrl.queryParameterNames());
//        Set<Integer> sentIds = httpUrl.queryParameterValues("shinryou-id").stream()
//                .map(Integer::parseInt).collect(Collectors.toSet());
//        assertEquals(shinryouIds, sentIds);
//    }
//
//    void assertEnterShinryou(int visitId, int shinryoucode, RecordedRequest req) {
//        ShinryouDTO expected = new ShinryouDTO();
//        expected.shinryouId = 0;
//        expected.visitId = visitId;
//        expected.shinryoucode = shinryoucode;
//        HttpUrl httpUrl = req.getRequestUrl();
//        ObjectMapper mapper = getObjectMapper();
//        try {
//            ShinryouDTO shinryou = mapper.readValue(req.getBody().readUtf8(), ShinryouDTO.class);
//            assertEquals("POST", req.getMethod());
//            assertEquals("/enter-shinryou", httpUrl.encodedPath());
//            assertEquals(expected, shinryou);
//        } catch(Exception ex){
//            throw new RuntimeException(ex);
//        }
//    }
}
