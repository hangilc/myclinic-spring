package jp.chang.myclinic.recordbrowser.tracking;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.recordbrowser.tracking.model.ModelRegistry;
import jp.chang.myclinic.recordbrowser.tracking.ui.TrackingRoot;
import jp.chang.myclinic.tracker.DispatchAction;

public class RecordDispatchAction implements DispatchAction {

    //private static Logger logger = LoggerFactory.getLogger(RecordDispatchAction.class);
    private ModelRegistry modelRegistry;
    private TrackingRoot root;

    public RecordDispatchAction(ModelRegistry modelRegistry, TrackingRoot root) {
        this.modelRegistry = modelRegistry;
        this.root = root;
    }

    @Override
    public void onVisitCreated(VisitDTO created, Runnable toNext) {
        modelRegistry.createRecord(created, () -> {
            root.scrollToCurrentVisit(2, toNext);
        });
    }

    @Override
    public void onVisitDeleted(VisitDTO deleted, Runnable toNext) {
        if (modelRegistry.deleteRecord(deleted.visitId)) {
            root.scrollToCurrentVisit(1, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onWqueueCreated(WqueueDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable toNext) {
        if( modelRegistry.updateWqueue(updated) ){
            root.scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onWqueueDeleted(WqueueDTO deleted, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onTextCreated(TextDTO created, Runnable toNext) {
        if( modelRegistry.createText(created) ){
            root.scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onTextUpdated(TextDTO prev, TextDTO updated, Runnable toNext) {
        if( modelRegistry.updateText(updated) ){
            root.scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onTextDeleted(TextDTO deleted, Runnable toNext) {
        if( modelRegistry.deleteText(deleted) ){
            root.scrollToCurrentVisit(1, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onDrugCreated(DrugDTO created, Runnable toNext) {
        modelRegistry.createDrug(created, () -> root.scrollToCurrentVisit(2, toNext));
    }

    @Override
    public void onDrugUpdated(DrugDTO prev, DrugDTO updated, Runnable toNext) {
        modelRegistry.updateDrug(updated, altered -> {
            if( altered ){
                root.scrollToCurrentVisit(2, toNext);
            } else {
                toNext.run();
            }
        });
    }

    @Override
    public void onDrugDeleted(DrugDTO deleted, Runnable toNext) {
        if( modelRegistry.deleteDrug(deleted) ){
            root.scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onShinryouCreated(ShinryouDTO created, Runnable toNext) {
        modelRegistry.createShinryou(created, () -> root.scrollToCurrentVisit(2, toNext));
    }

    @Override
    public void onShinryouDeleted(ShinryouDTO deleted, Runnable toNext) {
        if( modelRegistry.deleteShinryou(deleted) ){
            root.scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onConductCreated(ConductDTO created, Runnable toNext) {
        modelRegistry.createConduct(created);
        root.scrollToCurrentVisit(2, toNext);
    }

    @Override
    public void onConductUpdated(ConductDTO prev, ConductDTO updated, Runnable toNext) {
        if( modelRegistry.updateConduct(updated) ) {
            root.scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onConductDeleted(ConductDTO deleted, Runnable toNext) {
        if( modelRegistry.deleteConduct(deleted) ) {
            root.scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onGazouLabelCreated(GazouLabelDTO created, Runnable toNext) {
        if( modelRegistry.createGazouLabel(created) ){
            root.scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onGazouLabelUpdated(GazouLabelDTO prev, GazouLabelDTO updated, Runnable toNext) {
        if( modelRegistry.updateGazouLabel(updated) ){
            root.scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onGazouLabelDeleted(GazouLabelDTO deleted, Runnable toNext) {
        if( modelRegistry.deleteGazouLabel(deleted) ){
            root.scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onConductShinryouCreated(ConductShinryouDTO created, Runnable toNext) {
        modelRegistry.createConductShinryou(created, altered -> {
            if( altered ) {
                root.scrollToCurrentVisit(2, toNext);
            } else {
                toNext.run();
            }
        });
    }

    @Override
    public void onConductShinryouDeleted(ConductShinryouDTO deleted, Runnable toNext) {
        if( modelRegistry.deleteConductShinryou(deleted) ){
            root.scrollToCurrentVisit(1, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onConductDrugCreated(ConductDrugDTO created, Runnable toNext) {
        modelRegistry.createConductDrug(created, altered -> {
            if( altered ) {
                root.scrollToCurrentVisit(2, toNext);
            } else {
                toNext.run();
            }
        });
    }

    @Override
    public void onConductDrugDeleted(ConductDrugDTO deleted, Runnable toNext) {
        if( modelRegistry.deleteConductDrug(deleted) ){
            root.scrollToCurrentVisit(1, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onConductKizaiCreated(ConductKizaiDTO created, Runnable toNext) {
        modelRegistry.createConductKizai(created, altered -> {
            if( altered ) {
                root.scrollToCurrentVisit(2, toNext);
            } else {
                toNext.run();
            }
        });
    }

    @Override
    public void onConductKizaiDeleted(ConductKizaiDTO deleted, Runnable toNext) {
        if( modelRegistry.deleteConductKizai(deleted) ){
            root.scrollToCurrentVisit(1, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onShahokokuhoCreated(ShahokokuhoDTO created, Runnable toNext) {
        modelRegistry.createShahokokuho(created);
        toNext.run();
    }

    @Override
    public void onShahokokuhoUpdated(ShahokokuhoDTO prev, ShahokokuhoDTO updated, Runnable toNext) {
        modelRegistry.updateShahokokuho(updated);
        root.scrollToCurrentVisit(2, toNext);
    }

    @Override
    public void onShahokokuhoDeleted(ShahokokuhoDTO deleted, Runnable toNext) {
        modelRegistry.deleteShahokokuho(deleted);
        root.scrollToCurrentVisit(1, toNext);
    }

    @Override
    public void onKoukikoureiCreated(KoukikoureiDTO created, Runnable toNext) {
        modelRegistry.createKoukikourei(created);
        toNext.run();
    }

    @Override
    public void onKoukikoureiUpdated(KoukikoureiDTO prev, KoukikoureiDTO updated, Runnable toNext) {
        modelRegistry.updateKoukikourei(updated);
        root.scrollToCurrentVisit(2, toNext);
    }

    @Override
    public void onKoukikoureiDeleted(KoukikoureiDTO deleted, Runnable toNext) {
        modelRegistry.deleteKoukikourei(deleted);
        toNext.run();
    }

    @Override
    public void onKouhiCreated(KouhiDTO created, Runnable toNext) {
        modelRegistry.createKouhi(created);
        toNext.run();
    }

    @Override
    public void onKouhiUpdated(KouhiDTO prev, KouhiDTO updated, Runnable toNext) {
        modelRegistry.updateKouhi(updated);
        root.scrollToCurrentVisit(2, toNext);
    }

    @Override
    public void onKouhiDeleted(KouhiDTO deleted, Runnable toNext) {
        modelRegistry.deleteKouhi(deleted);
        toNext.run();
    }

    @Override
    public void onHokenUpdated(VisitDTO prev, VisitDTO updated, Runnable toNext) {
        modelRegistry.updateHoken(updated, altered -> {
            if( altered ){
                root.scrollToCurrentVisit(2, toNext);
            } else {
                toNext.run();
            }
        });
    }

    @Override
    public void onChargeCreated(ChargeDTO created, Runnable toNext) {
        if( modelRegistry.createCharge(created) ) {
            root.scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onChargeUpdated(ChargeDTO prev, ChargeDTO updated, Runnable toNext) {
        if( modelRegistry.updateCharge(updated) ) {
            root.scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onPaymentCreated(PaymentDTO created, Runnable toNext) {
        if( modelRegistry.createPayment(created) ) {
            root.scrollToCurrentVisit(2, toNext);
        } else {
            toNext.run();
        }
    }
}
