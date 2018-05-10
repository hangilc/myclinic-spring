package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.dto.DiseaseAdjDTO;
import jp.chang.myclinic.dto.DiseaseDTO;
import jp.chang.myclinic.dto.DiseaseNewDTO;
import jp.chang.myclinic.mastermap.ResolvedShinryouByoumei;
import jp.chang.myclinic.rcpt.builder.Clinic;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class TestCheckByoumei extends Base {

    @Test
    public void checkByoumei() {
        Set<Integer> shinryoucodes = shinryouByoumei.keySet();
        for (int shinryoucode : shinryoucodes) {
            doBaseBefore();
            testOne(shinryoucode);
        }
    }

    private void testOne(int shinryoucode) {
        Clinic clinic = new Clinic();
        int patientId = clinic.createPatient();
        clinic.startVisit(patientId);
        String startDate = clinic.getVisitedAt().substring(0, 10);
        clinic.addShinryou(shinryoucode);
        scope.visits = clinic.getVisits();
        new CheckByoumei(scope).check();
        ResolvedShinryouByoumei rsb = shinryouByoumei.get(shinryoucode).get(0);
        DiseaseNewDTO expected = RsbToDisease(rsb, patientId, startDate);
        assertEquals(1, nerror);
        assertEquals(List.of(expected), log.getEnteredDisseases());
    }

    private DiseaseNewDTO RsbToDisease(ResolvedShinryouByoumei rsb, int patientId, String startDate) {
        DiseaseNewDTO expected = new DiseaseNewDTO();
        DiseaseDTO disease = new DiseaseDTO();
        disease.diseaseId = 0;
        disease.shoubyoumeicode = rsb.byoumei.code;
        disease.patientId = patientId;
        disease.endReason = DiseaseEndReason.NotEnded.getCode();
        disease.startDate = startDate;
        disease.endDate = "0000-00-00";
        expected.disease = disease;
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
