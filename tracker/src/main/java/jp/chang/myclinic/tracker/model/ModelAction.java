package jp.chang.myclinic.tracker.model;

public interface ModelAction {

    default void onVisitCreated(Visit visit, Runnable toNext){ toNext.run(); }
    default void onVisitDeleted(int visitId, Runnable toNext){ toNext.run(); }
    default void onWqueueCreated(Wqueue wqueue, Runnable toNext){ toNext.run(); }
    default void onWqueueUpdated(Wqueue wqueue, Runnable toNext){ toNext. run(); }
    default void onWqueueDeleted(int visitId, Runnable toNext){ toNext.run(); }
    default void onTextCreated(Text text, Runnable toNext){ toNext.run(); }
}
