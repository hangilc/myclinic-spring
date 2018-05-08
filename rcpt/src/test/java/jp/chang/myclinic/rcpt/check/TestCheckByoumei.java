package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.dto.DiseaseAdjDTO;
import jp.chang.myclinic.dto.DiseaseNewDTO;
import jp.chang.myclinic.mastermap.ResolvedShinryouByoumei;
import jp.chang.myclinic.rcpt.builder.Clinic;
import jp.chang.myclinic.rcpt.builder.DiseaseBuilder;
import org.junit.Test;

import java.util.List;
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
        FixerLog log = new FixerLog();
        Scope scope = createScope(log);
        Clinic clinic = new Clinic();
        int patientId = clinic.createPatient();
        clinic.startVisit(patientId);
        String at = clinic.getVisitedAt();
        clinic.addShinryou(shinryoucode);
        scope.visits = clinic.getVisits();
        class State {
            private int nerror;
        }
        State state = new State();
        scope.errorHandler = err -> {
            state.nerror += 1;
            err.getFixFun().run();
        };
        new CheckByoumei(scope).check();
        ResolvedShinryouByoumei rsb = shinryouByoumei.get(shinryoucode).get(0);
        DiseaseNewDTO expected = RsbToDisease(rsb, patientId, at);
        assertEquals(1, state.nerror);
        assertEquals(List.of(expected), log.getEnteredDisseases());
    }

    private DiseaseNewDTO RsbToDisease(ResolvedShinryouByoumei rsb, int patientId, String at){
        DiseaseNewDTO expected = new DiseaseNewDTO();
        expected.disease = new DiseaseBuilder(d -> {
            d.diseaseId = 0;
            d.shoubyoumeicode = rsb.byoumei.code;
            d.patientId = patientId;
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
        return expected;
    }

}
