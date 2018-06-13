package jp.chang.myclinic.recordbrowser.tracking;

import jp.chang.myclinic.dto.*;

public interface DispatchAction {

    default void onTextCreated(TextDTO text, Runnable cb){ cb.run(); }
    default void onDrugCreated(DrugDTO created, Runnable cb){ cb.run(); }
    default void onShinryouCreated(ShinryouDTO created, Runnable cb){ cb.run(); }
    default void onConductCreated(ConductDTO created, Runnable cb){ cb.run(); }
    default void onGazouLabelCreated(GazouLabelDTO created, Runnable cb){ cb.run(); }
    default void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable cb){ cb.run(); }
    default void onConductShinryouCreated(ConductShinryouDTO created, Runnable cb){ cb.run(); };
    default void onConductDrugCreated(ConductDrugDTO created, Runnable toNext){ toNext.run(); };
    default void onConductKizaiCreated(ConductKizaiDTO created, Runnable toNext){ toNext.run(); };

}
