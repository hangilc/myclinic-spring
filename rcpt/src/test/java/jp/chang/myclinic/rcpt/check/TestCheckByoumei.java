package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.dto.DiseaseAdjDTO;
import jp.chang.myclinic.dto.DiseaseNewDTO;
import jp.chang.myclinic.mastermap.ResolvedShinryouByoumei;
import jp.chang.myclinic.rcpt.builder.DiseaseBuilder;
import jp.chang.myclinic.rcpt.builder.ShinryouFullBuilder;
import jp.chang.myclinic.rcpt.builder.VisitFull2Builder;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class TestCheckByoumei extends Base {

    @Test
    public void checkByoumei() throws Exception {
        Set<Integer> shinryoucodes = shinryouByoumei.keySet();
        for(int shinryoucode: shinryoucodes){
            testOne(shinryoucode);
        }
    }

    private void testOne(int shinryoucode) throws Exception {
        String at = getAt().toString();
        Scope scope = createScope();
        scope.visits.add(new VisitFull2Builder()
                .setVisitId(1000)
                .setPatientId(1234)
                .setVisitedAt(at)
                .addShinryou(new ShinryouFullBuilder()
                        .setShinryoucode(shinryoucode)
                        .build()
                )
                .build()
        );
        class State {
            private int nerror;
        }
        State state = new State();
        scope.errorHandler = err -> {
            state.nerror += 1;
            err.getFixFun().run();
        };
        MockWebServer server = getServer();
        server.enqueue(new MockResponse().setBody("1"));
        new CheckByoumei(scope).check();
        RecordedRequest req = server.takeRequest();
        DiseaseNewDTO sent = fromJson(req.getBody().readUtf8(), DiseaseNewDTO.class);
        DiseaseNewDTO expected = new DiseaseNewDTO();
        ResolvedShinryouByoumei rsb = shinryouByoumei.get(shinryoucode).get(0);
        expected.disease = new DiseaseBuilder(d -> {
            d.diseaseId = 0;
            d.shoubyoumeicode = rsb.byoumei.code;
            d.patientId = 1234;
            d.endReason = DiseaseEndReason.NotEnded.getCode();
            d.startDate = at;
            d.endDate = "0000-00-00";
        }).build();
        expected.adjList = rsb.shuushokugoList.stream()
                .map(s -> {
                    DiseaseAdjDTO adj = new DiseaseAdjDTO();
                    adj.shuushokugocode = s.code;
                    return adj;
                })
                .collect(Collectors.toList());
        assertEquals(1, state.nerror);
        assertEquals("POST", req.getMethod());
        assertEquals("/enter-disease", req.getPath());
        assertEquals(expected, sent);
    }

}
