package jp.chang.myclinic.recordbrowser.tracking;

import javafx.application.Platform;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.recordbrowser.tracking.model.ModelRegistry;
import jp.chang.myclinic.tracker.DispatchAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordDispatchAction implements DispatchAction {

    private static Logger logger = LoggerFactory.getLogger(RecordDispatchAction.class);
    private ModelRegistry modelRegistry;

    public RecordDispatchAction(ModelRegistry modelRegistry) {
        this.modelRegistry = modelRegistry;
    }

    @Override
    public void onVisitCreated(VisitDTO created, Runnable toNext) {
        modelRegistry.createRecord(created, toNext);
    }

    @Override
    public void onVisitDeleted(VisitDTO deleted, Runnable toNext) {
        Platform.runLater(() -> {
            modelRegistry.deleteRecord(deleted.visitId);
            toNext.run();
        });
    }

    @Override
    public void onWqueueCreated(WqueueDTO created, Runnable toNext) {
        toNext.run();
    }

    @Override
    public void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable toNext) {
        Platform.runLater(() -> {
            modelRegistry.updateWqueue(updated);
            toNext.run();
        });
    }

    @Override
    public void onWqueueDeleted(WqueueDTO deleted, Runnable toNext) {
        toNext.run();
    }
}
