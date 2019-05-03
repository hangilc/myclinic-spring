package jp.chang.myclinic.apitool.lib;

import jp.chang.myclinic.logdto.practicelog.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class PracticeLogKindList {

    public static Map<Class<?>, String> kindMap = new LinkedHashMap<>();

    static {
        kindMap.put(ChargeCreated.class, "charge-created");
        kindMap.put(ChargeDeleted.class, "charge-deleted");
        kindMap.put(ChargeUpdated.class, "charge-updated");
        kindMap.put(ConductCreated.class, "conduct-created");
        kindMap.put(ConductDeleted.class, "conduct-deleted");
        kindMap.put(ConductDrugCreated.class, "conduct-drug-created");
        kindMap.put(ConductDrugDeleted.class, "conduct-drug-deleted");
        kindMap.put(ConductDrugUpdated.class, "conduct-drug-updated");
        kindMap.put(ConductKizaiCreated.class, "conduct-kizai-created");
        kindMap.put(ConductKizaiDeleted.class, "conduct-kizai-deleted");
        kindMap.put(ConductKizaiUpdated.class, "conduct-kizai-updated");
        kindMap.put(ConductShinryouCreated.class, "conduct-shinryou-created");
        kindMap.put(ConductShinryouDeleted.class, "conduct-shinryou-deleted");
        kindMap.put(ConductShinryouUpdated.class, "conduct-shinryou-updated");
        kindMap.put(ConductUpdated.class, "conduct-updated");
        kindMap.put(DiseaseAdjCreated.class, "disease-adj-created");
        kindMap.put(DiseaseAdjDeleted.class, "disease-adj-deleted");
        kindMap.put(DiseaseAdjUpdated.class, "disease-adj-updated");
        kindMap.put(DiseaseCreated.class, "disease-created");
        kindMap.put(DiseaseDeleted.class, "disease-deleted");
        kindMap.put(DiseaseUpdated.class, "disease-updated");
        kindMap.put(DrugCreated.class, "drug-created");
        kindMap.put(DrugDeleted.class, "drug-deleted");
        kindMap.put(DrugUpdated.class, "drug-updated");
        kindMap.put(GazouLabelCreated.class, "gazou-label-created");
        kindMap.put(GazouLabelDeleted.class, "gazou-label-deleted");
        kindMap.put(GazouLabelUpdated.class, "gazou-label-updated");
        kindMap.put(KouhiCreated.class, "kouhi-created");
        kindMap.put(KouhiDeleted.class, "kouhi-deleted");
        kindMap.put(KouhiUpdated.class, "kouhi-updated");
        kindMap.put(KoukikoureiCreated.class, "koukikourei-created");
        kindMap.put(KoukikoureiDeleted.class, "koukikourei-deleted");
        kindMap.put(KoukikoureiUpdated.class, "koukikourei-updated");
        kindMap.put(PatientCreated.class, "patient-created");
        kindMap.put(PatientDeleted.class, "patient-deleted");
        kindMap.put(PatientUpdated.class, "patient-updated");
        kindMap.put(PaymentCreated.class, "payment-created");
        kindMap.put(PaymentDeleted.class, "payment-deleted");
        kindMap.put(PaymentUpdated.class, "payment-updated");
        kindMap.put(PharmaDrugCreated.class, "pharma-drug-created");
        kindMap.put(PharmaDrugDeleted.class, "pharma-drug-deleted");
        kindMap.put(PharmaDrugUpdated.class, "pharma-drug-updated");
        kindMap.put(PharmaQueueCreated.class, "pharma-queue-created");
        kindMap.put(PharmaQueueDeleted.class, "pharma-queue-deleted");
        kindMap.put(PharmaQueueUpdated.class, "pharma-queue-updated");
        kindMap.put(RoujinCreated.class, "roujin-created");
        kindMap.put(RoujinDeleted.class, "roujin-deleted");
        kindMap.put(RoujinUpdated.class, "roujin-updated");
        kindMap.put(ShahokokuhoCreated.class, "shahokokuho-created");
        kindMap.put(ShahokokuhoDeleted.class, "shahokokuho-deleted");
        kindMap.put(ShahokokuhoUpdated.class, "shahokokuho-updated");
        kindMap.put(ShinryouCreated.class, "shinryou-created");
        kindMap.put(ShinryouDeleted.class, "shinryou-deleted");
        kindMap.put(ShinryouUpdated.class, "shinryou-updated");
        kindMap.put(TextCreated.class, "text-created");
        kindMap.put(TextDeleted.class, "text-deleted");
        kindMap.put(TextUpdated.class, "text-updated");
        kindMap.put(VisitCreated.class, "visit-created");
        kindMap.put(VisitDeleted.class, "visit-deleted");
        kindMap.put(VisitUpdated.class, "visit-updated");
        kindMap.put(WqueueCreated.class, "wqueue-created");
        kindMap.put(WqueueDeleted.class, "wqueue-deleted");
        kindMap.put(WqueueUpdated.class, "wqueue-updated");
    }
}
