package jp.chang.myclinic.tracker;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.tracker.model.ModelAction;
import jp.chang.myclinic.tracker.model.ModelRegistry;
import jp.chang.myclinic.tracker.model.Visit;

class ModelDispatchAction implements DispatchAction {

    private ModelRegistry registry;
    private ModelAction modelAction;

    ModelDispatchAction(ModelRegistry registry, ModelAction modelAction){
        this.registry = registry;
        this.modelAction = modelAction;
    }

    @Override
    public void onVisitCreated(VisitDTO created, Runnable toNext) {
        Visit visit = registry.createVisit(created);
        modelAction.onVisitCreated(visit, toNext);
    }

    @Override
    public void onVisitDeleted(VisitDTO deleted, Runnable toNext) {
        registry.deleteVisit(deleted.visitId);
        modelAction.onVisitDeleted(deleted.visitId, toNext);
    }

    @Override
    public void onWqueueCreated(WqueueDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onWqueueDeleted(WqueueDTO deleted, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onTextCreated(TextDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onTextUpdated(TextDTO prev, TextDTO updated, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onTextDeleted(TextDTO deleted, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onDrugCreated(DrugDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onDrugUpdated(DrugDTO prev, DrugDTO updated, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onDrugDeleted(DrugDTO deleted, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onShinryouCreated(ShinryouDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onShinryouDeleted(ShinryouDTO deleted, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onConductCreated(ConductDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onConductUpdated(ConductDTO prev, ConductDTO updated, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onGazouLabelCreated(GazouLabelDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onGazouLabelUpdated(GazouLabelDTO prev, GazouLabelDTO updated, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onConductShinryouCreated(ConductShinryouDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onConductShinryouDeleted(ConductShinryouDTO deleted, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onConductDrugCreated(ConductDrugDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onConductDrugDeleted(ConductDrugDTO deleted, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onConductKizaiCreated(ConductKizaiDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onConductKizaiDeleted(ConductKizaiDTO deleted, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onChargeCreated(ChargeDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onChargeUpdated(ChargeDTO prev, ChargeDTO updated, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onPaymentCreated(PaymentDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onHokenUpdated(VisitDTO prev, VisitDTO updated, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onPharmaQueueCreated(PharmaQueueDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onPharmaQueueUpdated(PharmaQueueDTO prev, PharmaQueueDTO updated, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onPharmaQueueDeleted(PharmaQueueDTO deleted, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onPatientCreated(PatientDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onPatientUpdated(PatientDTO prev, PatientDTO updated, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onPatientDeleted(PatientDTO deleted, Runnable toNext) {
        toNext.run();
    }
}
