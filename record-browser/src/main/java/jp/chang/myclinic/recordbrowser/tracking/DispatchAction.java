package jp.chang.myclinic.recordbrowser.tracking;

import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.WqueueDTO;

public interface DispatchAction {

    default void onTextCreated(TextDTO text, Runnable cb){ cb.run(); }
    default void onDrugCreated(DrugDTO created, Runnable cb){ cb.run(); }
    default void onShinryouCreated(ShinryouDTO created, Runnable cb){ cb.run(); }
    default void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable cb){ cb.run(); }

}
