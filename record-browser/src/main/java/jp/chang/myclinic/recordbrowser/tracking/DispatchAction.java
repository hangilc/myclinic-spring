package jp.chang.myclinic.recordbrowser.tracking;

import jp.chang.myclinic.dto.*;

public interface DispatchAction {

    default void onTextCreated(TextDTO text, Runnable cb){ cb.run(); }
    default void onTextUpdated(TextDTO prev, TextDTO updated, Runnable cb){ cb.run(); }
    default void onTextDeleted(TextDTO deleted, Runnable cb){ cb.run(); }
    default void onDrugCreated(DrugDTO created, Runnable cb){ cb.run(); }
    default void onDrugUpdated(DrugDTO prev, DrugDTO updated, Runnable cb){ cb.run(); }
    default void onDrugDeleted(DrugDTO deleted, Runnable cb){ cb.run(); }
    default void onShinryouCreated(ShinryouDTO created, Runnable cb){ cb.run(); }
    default void onConductCreated(ConductDTO created, Runnable cb){ cb.run(); }
    default void onGazouLabelCreated(GazouLabelDTO created, Runnable cb){ cb.run(); }
    default void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable cb){ cb.run(); }
    default void onConductShinryouCreated(ConductShinryouDTO created, Runnable cb){ cb.run(); }
    default void onConductDrugCreated(ConductDrugDTO created, Runnable toNext){ toNext.run(); }
    default void onConductKizaiCreated(ConductKizaiDTO created, Runnable toNext){ toNext.run(); }
    default void onChargeCreated(ChargeDTO created, Runnable toNext){ toNext.run(); }
    default void onHokenUpdated(VisitDTO prev, VisitDTO updated, Runnable toNext){ toNext.run(); }
}
