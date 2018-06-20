package jp.chang.myclinic.pharma.tracker;

import jp.chang.myclinic.dto.*;

public interface DispatchAction {
    default void onVisitCreated(VisitDTO created, Runnable toNext){ toNext.run(); }
    default void onVisitDeleted(VisitDTO deleted, Runnable toNext){ toNext.run(); }
    default void onWqueueCreated(WqueueDTO created, Runnable toNext){ toNext.run(); }
    default void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable cb){ cb.run(); }
    default void onWqueueDeleted(WqueueDTO deleted, Runnable toNext){ toNext.run(); }
    default void onTextCreated(TextDTO created, Runnable cb){ cb.run(); }
    default void onTextUpdated(TextDTO prev, TextDTO updated, Runnable cb){ cb.run(); }
    default void onTextDeleted(TextDTO deleted, Runnable cb){ cb.run(); }
    default void onDrugCreated(DrugDTO created, Runnable cb){ cb.run(); }
    default void onDrugUpdated(DrugDTO prev, DrugDTO updated, Runnable cb){ cb.run(); }
    default void onDrugDeleted(DrugDTO deleted, Runnable cb){ cb.run(); }
    default void onShinryouCreated(ShinryouDTO created, Runnable cb){ cb.run(); }
    default void onShinryouDeleted(ShinryouDTO deleted, Runnable cb){ cb.run(); }
    default void onConductCreated(ConductDTO created, Runnable cb){ cb.run(); }
    default void onConductUpdated(ConductDTO prev, ConductDTO updated, Runnable cb){ cb.run(); }
    default void onGazouLabelCreated(GazouLabelDTO created, Runnable cb){ cb.run(); }
    default void onGazouLabelUpdated(GazouLabelDTO prev, GazouLabelDTO updated, Runnable cb){ cb.run(); }
    default void onConductShinryouCreated(ConductShinryouDTO created, Runnable cb){ cb.run(); }
    default void onConductShinryouDeleted(ConductShinryouDTO deleted, Runnable cb){ cb.run(); }
    default void onConductDrugCreated(ConductDrugDTO created, Runnable toNext){ toNext.run(); }
    default void onConductDrugDeleted(ConductDrugDTO deleted, Runnable cb){ cb.run(); }
    default void onConductKizaiCreated(ConductKizaiDTO created, Runnable toNext){ toNext.run(); }
    default void onConductKizaiDeleted(ConductKizaiDTO deleted, Runnable cb){ cb.run(); }
    default void onChargeCreated(ChargeDTO created, Runnable toNext){ toNext.run(); }
    default void onChargeUpdated(ChargeDTO prev, ChargeDTO updated, Runnable cb){ cb.run(); }
    default void onPaymentCreated(PaymentDTO created, Runnable toNext){ toNext.run(); }
    default void onHokenUpdated(VisitDTO prev, VisitDTO updated, Runnable toNext){ toNext.run(); }
    default void onPharmaQueueCreated(PharmaQueueDTO created, Runnable cb){ cb.run(); }
    default void onPharmaQueueUpdated(PharmaQueueDTO prev, PharmaQueueDTO updated, Runnable cb){ cb.run(); }
    default void onPharmaQueueDeleted(PharmaQueueDTO deleted, Runnable cb){ cb.run(); }
}
