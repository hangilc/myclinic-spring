package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.mastermap.ResolvedShinryouByoumei;
import jp.chang.myclinic.mastermap.generated.ResolvedDiseaseMap;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.Common;

import java.util.*;

import static org.junit.Assert.assertEquals;

class Base {

    //private static Logger logger = LoggerFactory.getLogger(Base.class);
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

    void assertBatchDeleteShinryou(int shinryouId, FixerLog log){
        assertEquals(1, log.getBatchDeletedShinryouList().size());
        assertEquals(Integer.valueOf(shinryouId), log.getBatchDeletedShinryouList().get(0).get(0));
    }

    void assertBatchDeleteShinryou(Set<Integer> shinryouIds, FixerLog log){
        assertEquals(1, log.getBatchDeletedShinryouList().size());
        assertEquals(shinryouIds,
                new HashSet<>(log.getBatchDeletedShinryouList().get(0)));
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

}
