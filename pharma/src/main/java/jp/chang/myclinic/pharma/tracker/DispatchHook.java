package jp.chang.myclinic.pharma.tracker;

import jp.chang.myclinic.pharma.tracker.model.PharmaQueue;
import jp.chang.myclinic.pharma.tracker.model.Visit;
import jp.chang.myclinic.pharma.tracker.model.Wqueue;

public interface DispatchHook {
    default void onVisitCreated(Visit created, Runnable toNext){ toNext.run(); }
    default void onVisitDeleted(int visitId, Runnable toNext){ toNext.run(); }
    default void onWqueueCreated(Wqueue created, Runnable toNext){ toNext.run(); }
    default void onWqueueUpdated(Wqueue updated, Runnable toNext){ toNext.run(); }
    default void onWqueueDeleted(int visitId, Runnable toNext){ toNext.run(); }
    default void onPharmaQueueCreated(PharmaQueue created, Runnable toNext){ toNext.run(); }
    default void onPharmaQueueUpdated(PharmaQueue updated, Runnable toNext){ toNext.run(); }
    default void onPharmaQueueDeleted(int visitId, Runnable toNext){ toNext.run(); }
}
