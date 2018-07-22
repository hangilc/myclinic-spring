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

}
