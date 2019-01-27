package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.rcpt.builder.Clinic;
import jp.chang.myclinic.rcpt.resolvedmap2.ResolvedMap;
import jp.chang.myclinic.rcpt.resolvedmap2.ResolvedShinryouMap;
import org.junit.Before;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

class Base {

    //private static Logger logger = LoggerFactory.getLogger(Base.class);
    ResolvedMap resolvedMap;
    ResolvedShinryouMap shinryouMap;
    int nerror;
    FixerLog log;
    Scope scope;

    @Before
    public void doBaseBefore(){
        nerror = 0;
        log = new FixerLog();
        scope = createScope(log);
    }

    Base() {
        this.resolvedMap = Listener.resolvedMap;
        this.shinryouMap = resolvedMap.shinryouMap;
    }

    private Scope createScope(FixerLog log) {
        Scope scope = new Scope();
        scope.visits = new ArrayList<>();
        scope.patient = new PatientDTO();
        scope.resolvedMasterMap = resolvedMap;
        scope.diseases = new ArrayList<>();
        scope.api = new FixerMock(log);
        scope.errorHandler = err -> {
            nerror += 1;
            log.getErrorMessages().add(err.getMessage());
            if( err.getFixMessage() != null ){
                log.getFixMessages().add(err.getFixMessage());
            }
            if( err.getFixFun() != null ){
                err.getFixFun().run();
            }
        };
        return scope;
    }

    void syncScope(Clinic clinic){
        scope.visits = clinic.getVisits();
        scope.diseases = clinic.getDiseases();
    }

    void assertBatchDeleteShinryou(int shinryouId) {
        assertEquals(1, log.getBatchDeletedShinryouList().size());
        assertEquals(Integer.valueOf(shinryouId), log.getBatchDeletedShinryouList().get(0).get(0));
    }

    void assertBatchDeleteShinryou(Set<Integer> shinryouIds) {
        assertEquals(1, log.getBatchDeletedShinryouList().size());
        assertEquals(shinryouIds,
                new HashSet<>(log.getBatchDeletedShinryouList().get(0)));
    }

    void assertEnterShinryou(int visitId, int shinryoucode) {
        List<ShinryouDTO> enteredList = log.getEnteredShinryouList();
        assertEquals(1, enteredList.size());
        ShinryouDTO entered = enteredList.get(0);
        ShinryouDTO expected = new ShinryouDTO();
        expected.shinryouId = 0;
        expected.shinryoucode = shinryoucode;
        expected.visitId = visitId;
        assertEquals(expected, entered);
    }

}
