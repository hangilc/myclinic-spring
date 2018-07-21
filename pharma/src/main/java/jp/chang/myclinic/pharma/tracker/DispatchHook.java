package jp.chang.myclinic.pharma.tracker;

import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.pharma.tracker.model.Visit;

public interface DispatchHook {
    default void onVisitCreated(Visit created, Runnable toNext){ toNext.run(); }
    default void onVisitDeleted(int visitId, Runnable toNext){ toNext.run(); }
    default void onWqueueUpdated(int visitId, WqueueWaitState waitState, Runnable toNext){ toNext.run(); }
    default void onWqueueDeleted(int visitId, Runnable toNext){ toNext.run(); }
    default void onPharmaQueueCreated(Visit visit, Runnable toNext){ toNext.run(); }
    default void onPharmaQueueDeleted(int visitId, Runnable toNext){ toNext.run(); }
}
