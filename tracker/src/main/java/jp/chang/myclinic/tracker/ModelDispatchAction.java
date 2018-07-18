package jp.chang.myclinic.tracker;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.tracker.model.*;

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
        Wqueue wqueue = registry.createWqueue(created);
        modelAction.onWqueueCreated(wqueue, toNext);
    }

    @Override
    public void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable toNext) {
        Wqueue wqueue = registry.updateWqueue(updated);
        if( wqueue != null ){
            modelAction.onWqueueUpdated(wqueue, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onWqueueDeleted(WqueueDTO deleted, Runnable toNext) {
        if( registry.deleteWqueue(deleted.visitId) ){
            modelAction.onWqueueDeleted(deleted.visitId, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onTextCreated(TextDTO created, Runnable toNext) {
        Text text = registry.createText(created);
        modelAction.onTextCreated(text, toNext);
    }

    @Override
    public void onTextUpdated(TextDTO prev, TextDTO updated, Runnable toNext) {
        Text text = null;
        Visit visit = registry.getVisit(updated.visitId);
        if( visit != null ){
            for(Text t: visit.getTexts()){
                if( t.getTextId() == updated.textId ){
                    text = t;
                    break;
                }
            }
        }
        if( text != null ){
            text.update(updated);
            modelAction.onTextUpdated(text, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onTextDeleted(TextDTO deleted, Runnable toNext) {
        Visit visit = registry.getVisit(deleted.visitId);
        if( visit != null && visit.getTexts().removeIf(t -> t.getTextId() == deleted.textId) ){
            modelAction.onTextDeleted(deleted.textId, toNext);
        } else {
            toNext.run();
        }
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
