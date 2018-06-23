package jp.chang.myclinic.hotline.tracker;

import jp.chang.myclinic.dto.HotlineDTO;

public interface DispatchAction {

    default public void onHotlineCreated(HotlineDTO created, Runnable toNext){ toNext.run(); }

}
