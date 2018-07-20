package jp.chang.myclinic.recordbrowser.tracking;

import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.WqueueDTO;
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
}
