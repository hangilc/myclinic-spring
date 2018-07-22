package jp.chang.myclinic.tracker;

import jp.chang.myclinic.dto.*;

public interface DispatchAction {
    default void onVisitCreated(VisitDTO created, Runnable toNext){ toNext.run(); }
    default void onVisitDeleted(VisitDTO deleted, Runnable toNext){ toNext.run(); }
    default void onWqueueCreated(WqueueDTO created, Runnable toNext){ toNext.run(); }
    default void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated, Runnable toNext){ toNext.run(); }
    default void onWqueueDeleted(WqueueDTO deleted, Runnable toNext){ toNext.run(); }
    default void onTextCreated(TextDTO created, Runnable toNext){ toNext.run(); }
    default void onTextUpdated(TextDTO prev, TextDTO updated, Runnable toNext){ toNext.run(); }
    default void onTextDeleted(TextDTO deleted, Runnable toNext){ toNext.run(); }
    default void onDrugCreated(DrugDTO created, Runnable toNext){ toNext.run(); }
    default void onDrugUpdated(DrugDTO prev, DrugDTO updated, Runnable toNext){ toNext.run(); }
    default void onDrugDeleted(DrugDTO deleted, Runnable toNext){ toNext.run(); }
    default void onShinryouCreated(ShinryouDTO created, Runnable toNext){ toNext.run(); }
    default void onShinryouDeleted(ShinryouDTO deleted, Runnable toNext){ toNext.run(); }
    default void onConductCreated(ConductDTO created, Runnable toNext){ toNext.run(); }
    default void onConductUpdated(ConductDTO prev, ConductDTO updated, Runnable toNext){ toNext.run(); }
    default void onConductDeleted(ConductDTO deleted, Runnable toNext){ toNext.run(); }
    default void onGazouLabelCreated(GazouLabelDTO created, Runnable toNext){ toNext.run(); }
    default void onGazouLabelUpdated(GazouLabelDTO prev, GazouLabelDTO updated, Runnable toNext){ toNext.run(); }
    default void onGazouLabelDeleted(GazouLabelDTO deleted, Runnable toNext){ toNext.run(); }
    default void onConductShinryouCreated(ConductShinryouDTO created, Runnable toNext){ toNext.run(); }
    default void onConductShinryouDeleted(ConductShinryouDTO deleted, Runnable toNext){ toNext.run(); }
    default void onConductDrugCreated(ConductDrugDTO created, Runnable toNext){ toNext.run(); }
    default void onConductDrugDeleted(ConductDrugDTO deleted, Runnable toNext){ toNext.run(); }
    default void onConductKizaiCreated(ConductKizaiDTO created, Runnable toNext){ toNext.run(); }
    default void onConductKizaiDeleted(ConductKizaiDTO deleted, Runnable toNext){ toNext.run(); }
    default void onChargeCreated(ChargeDTO created, Runnable toNext){ toNext.run(); }
    default void onChargeUpdated(ChargeDTO prev, ChargeDTO updated, Runnable toNext){ toNext.run(); }
    default void onPaymentCreated(PaymentDTO created, Runnable toNext){ toNext.run(); }
    default void onHokenUpdated(VisitDTO prev, VisitDTO updated, Runnable toNext){ toNext.run(); }
    default void onPharmaQueueCreated(PharmaQueueDTO created, Runnable toNext){ toNext.run(); }
    default void onPharmaQueueUpdated(PharmaQueueDTO prev, PharmaQueueDTO updated, Runnable toNext){ toNext.run(); }
    default void onPharmaQueueDeleted(PharmaQueueDTO deleted, Runnable toNext){ toNext.run(); }
    default void onPatientCreated(PatientDTO created, Runnable toNext){ toNext.run(); }
    default void onPatientUpdated(PatientDTO prev, PatientDTO updated, Runnable toNext){ toNext.run(); }
    default void onPatientDeleted(PatientDTO deleted, Runnable toNext){ toNext.run(); }
}
