package jp.chang.myclinic.pharma.tracker;

import jp.chang.myclinic.dto.PharmaQueueDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import jp.chang.myclinic.pharma.tracker.model.PharmaQueue;
import jp.chang.myclinic.pharma.tracker.model.Wqueue;

class ActionHook implements DispatchAction {

    private ModelRegistry registry;
    private DispatchHook hook;

    ActionHook(ModelRegistry registry, DispatchHook hook) {
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
        Wqueue wqueue = registry.createWqueue(created);
        hook.onWqueueCreated(wqueue, toNext);
    }

    @Override
    public void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable cb) {
        Wqueue wqueue = registry.getWqueue(updated.visitId);
        if (wqueue != null) {
            wqueue.setWaitState(updated.waitState);
            hook.onWqueueUpdated(wqueue, cb);
        } else {
            cb.run();
        }
    }

    @Override
    public void onWqueueDeleted(WqueueDTO deleted, Runnable toNext) {
        registry.deleteWqueue(deleted.visitId);
        hook.onWqueueDeleted(deleted.visitId, toNext);
    }

    @Override
    public void onPharmaQueueCreated(PharmaQueueDTO created, Runnable toNext) {
        PharmaQueue pharmaQueue = registry.createPharmaQueue(created);
        hook.onPharmaQueueCreated(pharmaQueue, toNext);
    }

    @Override
    public void onPharmaQueueDeleted(PharmaQueueDTO deleted, Runnable toNext) {
        registry.deletePharmaQueue(deleted.visitId);
        hook.onPharmaQueueDeleted(deleted.visitId, toNext);
    }

}
