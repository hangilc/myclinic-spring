package jp.chang.myclinic.recordbrowser.tracking;

import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.WqueueDTO;

public interface DispatchAction {

    default void onTextCreated(TextDTO text){}
    default void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated){ }

}
