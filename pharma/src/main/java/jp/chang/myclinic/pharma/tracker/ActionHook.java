package jp.chang.myclinic.pharma.tracker;

import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.PharmaQueueDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import jp.chang.myclinic.pharma.tracker.model.Visit;
import jp.chang.myclinic.tracker.DispatchAction;

public class ActionHook implements DispatchAction {

    private ModelRegistry registry;
    private DispatchHook hook;

    public ActionHook(ModelRegistry registry, DispatchHook hook) {
        this.registry = registry;
        this.hook = hook;
    }

    @Override
    public void onVisitCreated(VisitDTO created, Runnable toNext) {
        registry.createVisit(created)
                .thenAccept(visit -> hook.onVisitCreated(visit, toNext))
                .exceptionally(HandlerFX::exceptionally);
    }

    @Override
    public void onVisitDeleted(VisitDTO deleted, Runnable toNext) {
        registry.deleteVisit(deleted.visitId);
        hook.onVisitDeleted(deleted.visitId, toNext);
    }

    @Override
    public void onWqueueCreated(WqueueDTO created, Runnable toNext) {
        Visit visit = registry.getVisit(created.visitId);
        if( visit != null ){
            visit.setWqueueState(WqueueWaitState.fromCode(created.waitState));
        }
        hook.onWqueueUpdated(created.visitId, WqueueWaitState.fromCode(created.waitState), toNext);
    }

    @Override
    public void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable toNext) {
        Visit visit = registry.getVisit(updated.visitId);
        if( visit != null ){
            visit.setWqueueState(WqueueWaitState.fromCode(updated.waitState));
        }
        hook.onWqueueUpdated(updated.visitId, WqueueWaitState.fromCode(updated.waitState), toNext);
    }

    @Override
    public void onWqueueDeleted(WqueueDTO deleted, Runnable toNext) {
        Visit visit = registry.getVisit(deleted.visitId);
        if( visit != null ){
            visit.setWqueueState(null);
        }
        hook.onWqueueDeleted(deleted.visitId, toNext);
    }

    @Override
    public void onPharmaQueueCreated(PharmaQueueDTO created, Runnable toNext) {
        Visit visit = registry.getVisit(created.visitId);
        if( visit != null ) {
            hook.onPharmaQueueCreated(visit, toNext);
        } else {
            toNext.run();
        }
    }

    @Override
    public void onPharmaQueueDeleted(PharmaQueueDTO deleted, Runnable toNext) {
        hook.onPharmaQueueDeleted(deleted.visitId, toNext);
    }

}
