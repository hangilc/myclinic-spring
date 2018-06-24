package jp.chang.myclinic.hotline.tracker;

import jp.chang.myclinic.dto.HotlineDTO;

public interface DispatchAction {

    default void onHotlineCreated(HotlineDTO created, Runnable toNext){ toNext.run(); }
    default void onHotlineBeep(String receiver, Runnable toNext){ toNext.run(); }

}
