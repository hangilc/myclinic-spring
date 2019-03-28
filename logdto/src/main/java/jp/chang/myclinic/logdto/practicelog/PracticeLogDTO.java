package jp.chang.myclinic.logdto.practicelog;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.dto.annotation.AutoInc;
import jp.chang.myclinic.dto.annotation.Primary;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public class PracticeLogDTO {
    @Primary
    @AutoInc
    public int serialId;
    public String createdAt;
    public String kind;
    public String body;

    public PracticeLogDTO() {
    }

    public PracticeLogDTO(int serialId, String createdAt, String kind, String body) {
        this.serialId = serialId;
        this.createdAt = createdAt;
        this.kind = kind;
        this.body = body;
    }

    @Override
    public String toString() {
        return "PracticeLogDTO{" +
                "serialId=" + serialId +
                ", createdAt='" + createdAt + '\'' +
                ", kind='" + kind + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    private static ObjectMapper mapper = new ObjectMapper();

    public boolean isVisitCreated() {
        return "visit-created".equals(kind);
    }

    public VisitCreated asVisitCreated() {
        if (!"visit-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, VisitCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isVisitDeleted() {
        return "visit-deleted".equals(kind);
    }

    public VisitDeleted asVisitDeleted() {
        if (!"visit-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, VisitDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isVisitUpdated() {
        return "hoken-updated".equals(kind);
    }

    public VisitUpdated asVisitUpdated() {
        if (!"hoken-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, VisitUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isWqueueCreated() {
        return "wqueue-created".equals(kind);
    }

    public WqueueCreated asWqueueCreated() {
        if (!"wqueue-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, WqueueCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isWqueueDeleted() {
        return "wqueue-deleted".equals(kind);
    }

    public WqueueDeleted asWqueueDeleted() {
        if (!"wqueue-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, WqueueDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isWqueueUpdated() {
        return "wqueue-updated".equals(kind);
    }

    public WqueueUpdated asWqueueUpdated() {
        if (!"wqueue-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, WqueueUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isTextUpdated() {
        return "text-updated".equals(kind);
    }

    public TextUpdated asTextUpdated() {
        if (!"text-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, TextUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isTextCreated() {
        return "text-created".equals(kind);
    }

    public TextCreated asTextCreated() {
        if (!"text-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, TextCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isTextDeleted() {
        return "text-deleted".equals(kind);
    }

    public TextDeleted asTextDeleted() {
        if (!"text-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, TextDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isPharmaQueueCreated() {
        return "pharma-queue-created".equals(kind);
    }

    public PharmaQueueCreated asPharmaQueueCreated() {
        if (!"pharma-queue-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, PharmaQueueCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isPharmaQueueUpdated() {
        return "pharma-queue-updated".equals(kind);
    }

    public PharmaQueueUpdated asPharmaQueueUpdated() {
        if (!"pharma-queue-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, PharmaQueueUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isPharmaQueueDeleted() {
        return "pharma-queue-deleted".equals(kind);
    }

    public PharmaQueueDeleted asPharmaQueueDeleted() {
        if (!"pharma-queue-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, PharmaQueueDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isPatientCreated() {
        return "patient-created".equals(kind);
    }

    public PatientCreated asPatientCreated() {
        if (!"patient-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, PatientCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isPatientUpdated() {
        return "patient-updated".equals(kind);
    }

    public PatientUpdated asPatientUpdated() {
        if (!"patient-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, PatientUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isPatientDeleted() {
        return "patient-deleted".equals(kind);
    }

    public PatientDeleted asPatientDeleted() {
        if (!"patient-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, PatientDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isShahokokuhoCreated() {
        return "shahokokuho-created".equals(kind);
    }

    public ShahokokuhoCreated asShahokokuhoCreated() {
        if (!"shahokokuho-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ShahokokuhoCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isShahokokuhoUpdated() {
        return "shahokokuho-updated".equals(kind);
    }

    public ShahokokuhoUpdated asShahokokuhoUpdated() {
        if (!"shahokokuho-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ShahokokuhoUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isShahokokuhoDeleted() {
        return "shahokokuho-deleted".equals(kind);
    }

    public ShahokokuhoDeleted asShahokokuhoDeleted() {
        if (!"shahokokuho-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ShahokokuhoDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isKoukikoureiCreated() {
        return "koukikourei-created".equals(kind);
    }

    public KoukikoureiCreated asKoukikoureiCreated() {
        if (!"koukikourei-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, KoukikoureiCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isKoukikoureiUpdated() {
        return "koukikourei-updated".equals(kind);
    }

    public KoukikoureiUpdated asKoukikoureiUpdated() {
        if (!"koukikourei-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, KoukikoureiUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isKoukikoureiDeleted() {
        return "koukikourei-deleted".equals(kind);
    }

    public KoukikoureiDeleted asKoukikoureiDeleted() {
        if (!"koukikourei-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, KoukikoureiDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isRoujinCreated() {
        return "roujin-created".equals(kind);
    }

    public RoujinCreated asRoujinCreated() {
        if (!"roujin-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, RoujinCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isRoujinUpdated() {
        return "roujin-updated".equals(kind);
    }

    public RoujinUpdated asRoujinUpdated() {
        if (!"roujin-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, RoujinUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isRoujinDeleted() {
        return "roujin-deleted".equals(kind);
    }

    public RoujinDeleted asRoujinDeleted() {
        if (!"roujin-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, RoujinDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isKouhiCreated() {
        return "kouhi-created".equals(kind);
    }

    public KouhiCreated asKouhiCreated() {
        if (!"kouhi-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, KouhiCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isKouhiUpdated() {
        return "kouhi-updated".equals(kind);
    }

    public KouhiUpdated asKouhiUpdated() {
        if (!"kouhi-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, KouhiUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isKouhiDeleted() {
        return "kouhi-deleted".equals(kind);
    }

    public KouhiDeleted asKouhiDeleted() {
        if (!"kouhi-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, KouhiDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isChargeCreated() {
        return "charge-created".equals(kind);
    }

    public ChargeCreated asChargeCreated() {
        if (!"charge-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ChargeCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isChargeUpdated() {
        return "charge-updated".equals(kind);
    }

    public ChargeUpdated asChargeUpdated() {
        if (!"charge-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ChargeUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isChargeDeleted() {
        return "charge-deleted".equals(kind);
    }

    public ChargeDeleted asChargeDeleted() {
        if (!"charge-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ChargeDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isPaymentCreated() {
        return "payment-created".equals(kind);
    }

    public PaymentCreated asPaymentCreated() {
        if (!"payment-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, PaymentCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isPaymentUpdated() {
        return "payment-updated".equals(kind);
    }

    public PaymentUpdated asPaymentUpdated() {
        if (!"payment-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, PaymentUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isPaymentDeleted() {
        return "payment-deleted".equals(kind);
    }

    public PaymentDeleted asPaymentDeleted() {
        if (!"payment-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, PaymentDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isShinryouCreated() {
        return "shinryou-created".equals(kind);
    }

    public ShinryouCreated asShinryouCreated() {
        if (!"shinryou-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ShinryouCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isShinryouUpdated() {
        return "shinryou-updated".equals(kind);
    }

    public ShinryouUpdated asShinryouUpdated() {
        if (!"shinryou-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ShinryouUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isShinryouDeleted() {
        return "shinryou-deleted".equals(kind);
    }

    public ShinryouDeleted asShinryouDeleted() {
        if (!"shinryou-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ShinryouDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isDrugCreated() {
        return "drug-created".equals(kind);
    }

    public DrugCreated asDrugCreated() {
        if (!"drug-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, DrugCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isDrugUpdated() {
        return "drug-updated".equals(kind);
    }

    public DrugUpdated asDrugUpdated() {
        if (!"drug-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, DrugUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isDrugDeleted() {
        return "drug-deleted".equals(kind);
    }

    public DrugDeleted asDrugDeleted() {
        if (!"drug-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, DrugDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isGazouLabelCreated() {
        return "gazou-label-created".equals(kind);
    }

    public GazouLabelCreated asGazouLabelCreated() {
        if (!"gazou-label-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, GazouLabelCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isGazouLabelUpdated() {
        return "gazou-label-updated".equals(kind);
    }

    public GazouLabelUpdated asGazouLabelUpdated() {
        if (!"gazou-label-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, GazouLabelUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isGazouLabelDeleted() {
        return "gazou-label-deleted".equals(kind);
    }

    public GazouLabelDeleted asGazouLabelDeleted() {
        if (!"gazou-label-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, GazouLabelDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isConductCreated() {
        return "conduct-created".equals(kind);
    }

    public ConductCreated asConductCreated() {
        if (!"conduct-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ConductCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isConductUpdated() {
        return "conduct-updated".equals(kind);
    }

    public ConductUpdated asConductUpdated() {
        if (!"conduct-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ConductUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isConductDeleted() {
        return "conduct-deleted".equals(kind);
    }

    public ConductDeleted asConductDeleted() {
        if (!"conduct-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ConductDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isConductShinryouCreated() {
        return "conduct-shinryou-created".equals(kind);
    }

    public ConductShinryouCreated asConductShinryouCreated() {
        if (!"conduct-shinryou-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ConductShinryouCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isConductShinryouUpdated() {
        return "conduct-shinryou-updated".equals(kind);
    }

    public ConductShinryouUpdated asConductShinryouUpdated() {
        if (!"conduct-shinryou-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ConductShinryouUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isConductShinryouDeleted() {
        return "conduct-shinryou-deleted".equals(kind);
    }

    public ConductShinryouDeleted asConductShinryouDeleted() {
        if (!"conduct-shinryou-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ConductShinryouDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isConductDrugCreated() {
        return "conduct-drug-created".equals(kind);
    }

    public ConductDrugCreated asConductDrugCreated() {
        if (!"conduct-drug-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ConductDrugCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isConductDrugUpdated() {
        return "conduct-drug-updated".equals(kind);
    }

    public ConductDrugUpdated asConductDrugUpdated() {
        if (!"conduct-drug-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ConductDrugUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isConductDrugDeleted() {
        return "conduct-drug-deleted".equals(kind);
    }

    public ConductDrugDeleted asConductDrugDeleted() {
        if (!"conduct-drug-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ConductDrugDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isConductKizaiCreated() {
        return "conduct-kizai-created".equals(kind);
    }

    public ConductKizaiCreated asConductKizaiCreated() {
        if (!"conduct-kizai-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ConductKizaiCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isConductKizaiUpdated() {
        return "conduct-kizai-updated".equals(kind);
    }

    public ConductKizaiUpdated asConductKizaiUpdated() {
        if (!"conduct-kizai-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ConductKizaiUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isConductKizaiDeleted() {
        return "conduct-kizai-deleted".equals(kind);
    }

    public ConductKizaiDeleted asConductKizaiDeleted() {
        if (!"conduct-kizai-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, ConductKizaiDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isPharmaDrugCreated() {
        return "pharma-drug-created".equals(kind);
    }

    public PharmaDrugCreated asPharmaDrugCreated() {
        if (!"pharma-drug-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, PharmaDrugCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isPharmaDrugUpdated() {
        return "pharma-drug-updated".equals(kind);
    }

    public PharmaDrugUpdated asPharmaDrugUpdated() {
        if (!"pharma-drug-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, PharmaDrugUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isPharmaDrugDeleted() {
        return "pharma-drug-deleted".equals(kind);
    }

    public PharmaDrugDeleted asPharmaDrugDeleted() {
        if (!"pharma-drug-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, PharmaDrugDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isDiseaseCreated() {
        return "disease-created".equals(kind);
    }

    public DiseaseCreated asDiseaseCreated() {
        if (!"disease-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, DiseaseCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isDiseaseUpdated() {
        return "disease-updated".equals(kind);
    }

    public DiseaseUpdated asDiseaseUpdated() {
        if (!"disease-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, DiseaseUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isDiseaseDeleted() {
        return "disease-deleted".equals(kind);
    }

    public DiseaseDeleted asDiseaseDeleted() {
        if (!"disease-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, DiseaseDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public boolean isDiseaseAdjCreated() {
        return "disease-adj-created".equals(kind);
    }

    public DiseaseAdjCreated asDiseaseAdjCreated() {
        if (!"disease-adj-created".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, DiseaseAdjCreated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isDiseaseAdjUpdated() {
        return "disease-adj-updated".equals(kind);
    }

    public DiseaseAdjUpdated asDiseaseAdjUpdated() {
        if (!"disease-adj-updated".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, DiseaseAdjUpdated.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isDiseaseAdjDeleted() {
        return "disease-adj-deleted".equals(kind);
    }

    public DiseaseAdjDeleted asDiseaseAdjDeleted() {
        if (!"disease-adj-deleted".equals(kind)) {
            throw new RuntimeException("Invalid practice-log kind.");
        }
        try {
            return mapper.readValue(body, DiseaseAdjDeleted.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
