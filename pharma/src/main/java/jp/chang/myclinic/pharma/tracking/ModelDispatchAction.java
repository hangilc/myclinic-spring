package jp.chang.myclinic.pharma.tracking;

import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaQueueDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.tracker.DispatchAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelDispatchAction implements DispatchAction {

    //private static Logger logger = LoggerFactory.getLogger(ModelDispatchAction.class);
    @Autowired
    private ModelRegistry registry;

    public ModelDispatchAction() {

    }

    @Override
    public void onVisitCreated(VisitDTO created, Runnable toNext) {
        registry.createVisit(created, toNext);
    }

    @Override
    public void onVisitDeleted(VisitDTO deleted, Runnable toNext) {
        registry.deleteVisit(deleted.visitId);
        toNext.run();
    }

    @Override
    public void onWqueueCreated(WqueueDTO created, Runnable toNext) {
        registry.setWqueueWaitState(created.visitId, WqueueWaitState.fromCode(created.waitState));
        toNext.run();
    }

    @Override
    public void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable toNext) {
        registry.setWqueueWaitState(updated.visitId, WqueueWaitState.fromCode(updated.waitState));
        toNext.run();
    }

    @Override
    public void onWqueueDeleted(WqueueDTO deleted, Runnable toNext) {
        registry.setWqueueWaitState(deleted.visitId, null);
        toNext.run();
    }

    @Override
    public void onPharmaQueueCreated(PharmaQueueDTO created, Runnable toNext) {
        registry.addToPharmaQueue(created.visitId);
        toNext.run();
    }

    @Override
    public void onPharmaQueueDeleted(PharmaQueueDTO deleted, Runnable toNext) {
        registry.removeFromPharmaQueue(deleted.visitId);
    }

    @Override
    public void onPatientCreated(PatientDTO created, Runnable toNext) {
        registry.createPatient(created);
        toNext.run();
    }

    @Override
    public void onPatientUpdated(PatientDTO prev, PatientDTO updated, Runnable toNext) {
        registry.updatePatient(updated);
        toNext.run();
    }

    @Override
    public void onPatientDeleted(PatientDTO deleted, Runnable toNext) {
        registry.deletePatient(deleted.patientId);
        toNext.run();
    }
}
