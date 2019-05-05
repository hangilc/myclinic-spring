package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductDTO;
import jp.chang.myclinic.dto.GazouLabelDTO;
import jp.chang.myclinic.dto.VisitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class GazouLabelTester extends TesterBase {

    public GazouLabelTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    private void withConduct(Consumer<Integer> consumer){
        VisitDTO visit = startExam();
        ConductDTO conduct = new ConductDTO();
        conduct.visitId = visit.visitId;
        conduct.kind = ConductKind.Gazou.getCode();
        dbBackend.txProc(backend -> backend.enterConduct(conduct));
        consumer.accept(conduct.conductId);
        endExam(visit.visitId, 0);
    }

    private GazouLabelDTO create(int conductId, String label){
        GazouLabelDTO gazouLabel = new GazouLabelDTO();
        gazouLabel.conductId = conductId;
        gazouLabel.label = label;
        return gazouLabel;
    }

    @DbTest
    public void testEnter(){
        withConduct(conductId -> {
            dbBackendService.setGazouLabel(conductId, "胸部単純撮影");
            confirm(dbBackend.query(backend -> backend.getGazouLabel(conductId)).label.equals("胸部単純撮影"));
        });
    }

    @DbTest
    public void testUpdate(){
        withConduct(conductId -> {
            dbBackendService.setGazouLabel(conductId, "胸部単純撮影");
            dbBackendService.setGazouLabel(conductId, "骨塩定量");
            confirm(dbBackend.query(backend -> backend.getGazouLabel(conductId)).label.equals("骨塩定量"));
        });
    }

    @DbTest
    public void testClear(){
        withConduct(conductId -> {
            dbBackendService.setGazouLabel(conductId, "胸部単純撮影");
            dbBackendService.setGazouLabel(conductId, "");
            confirm(dbBackend.query(backend -> backend.getGazouLabel(conductId)) == null);
        });
        withConduct(conductId -> {
            dbBackendService.setGazouLabel(conductId, "胸部単純撮影");
            dbBackendService.setGazouLabel(conductId, null);
            confirm(dbBackend.query(backend -> backend.getGazouLabel(conductId)) == null);
        });
    }

}
