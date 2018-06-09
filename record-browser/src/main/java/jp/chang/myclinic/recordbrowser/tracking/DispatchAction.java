package jp.chang.myclinic.recordbrowser.tracking;

import jp.chang.myclinic.dto.WqueueDTO;

public interface DispatchAction {

    default void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated){ }

}
