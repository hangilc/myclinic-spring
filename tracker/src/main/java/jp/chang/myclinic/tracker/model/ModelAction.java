package jp.chang.myclinic.tracker.model;

public interface ModelAction {

    default void onVisitCreated(Visit visit, Runnable toNext){ toNext.run(); }
    default void onVisitDeleted(int visitId, Runnable toNext){ toNext.run(); }
}
