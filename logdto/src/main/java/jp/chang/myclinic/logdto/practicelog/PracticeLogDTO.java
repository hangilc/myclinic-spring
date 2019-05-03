package jp.chang.myclinic.logdto.practicelog;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.dto.annotation.AutoInc;
import jp.chang.myclinic.dto.annotation.Primary;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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
        return "PracticeLogDTO{" + "serialId=" + serialId + ", createdAt='" + createdAt + '\'' + ", kind='" + kind + '\'' + ", body='" + body + '\'' + '}';
    }

    private static ObjectMapper mapper = new ObjectMapper();

    public boolean isChargeCreated() {
        return "charge-created".equals(kind);
    }

    public Optional<ChargeCreated> getChargeCreated() {
        if (isChargeCreated())
            try {
                return Optional.of(mapper.readValue(body, ChargeCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ChargeCreated asChargeCreated() {
        return getChargeCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isChargeDeleted() {
        return "charge-deleted".equals(kind);
    }

    public Optional<ChargeDeleted> getChargeDeleted() {
        if (isChargeDeleted())
            try {
                return Optional.of(mapper.readValue(body, ChargeDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ChargeDeleted asChargeDeleted() {
        return getChargeDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isChargeUpdated() {
        return "charge-updated".equals(kind);
    }

    public Optional<ChargeUpdated> getChargeUpdated() {
        if (isChargeUpdated())
            try {
                return Optional.of(mapper.readValue(body, ChargeUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ChargeUpdated asChargeUpdated() {
        return getChargeUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isConductCreated() {
        return "conduct-created".equals(kind);
    }

    public Optional<ConductCreated> getConductCreated() {
        if (isConductCreated())
            try {
                return Optional.of(mapper.readValue(body, ConductCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ConductCreated asConductCreated() {
        return getConductCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isConductDeleted() {
        return "conduct-deleted".equals(kind);
    }

    public Optional<ConductDeleted> getConductDeleted() {
        if (isConductDeleted())
            try {
                return Optional.of(mapper.readValue(body, ConductDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ConductDeleted asConductDeleted() {
        return getConductDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isConductDrugCreated() {
        return "conduct-drug-created".equals(kind);
    }

    public Optional<ConductDrugCreated> getConductDrugCreated() {
        if (isConductDrugCreated())
            try {
                return Optional.of(mapper.readValue(body, ConductDrugCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ConductDrugCreated asConductDrugCreated() {
        return getConductDrugCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isConductDrugDeleted() {
        return "conduct-drug-deleted".equals(kind);
    }

    public Optional<ConductDrugDeleted> getConductDrugDeleted() {
        if (isConductDrugDeleted())
            try {
                return Optional.of(mapper.readValue(body, ConductDrugDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ConductDrugDeleted asConductDrugDeleted() {
        return getConductDrugDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isConductDrugUpdated() {
        return "conduct-drug-updated".equals(kind);
    }

    public Optional<ConductDrugUpdated> getConductDrugUpdated() {
        if (isConductDrugUpdated())
            try {
                return Optional.of(mapper.readValue(body, ConductDrugUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ConductDrugUpdated asConductDrugUpdated() {
        return getConductDrugUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isConductKizaiCreated() {
        return "conduct-kizai-created".equals(kind);
    }

    public Optional<ConductKizaiCreated> getConductKizaiCreated() {
        if (isConductKizaiCreated())
            try {
                return Optional.of(mapper.readValue(body, ConductKizaiCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ConductKizaiCreated asConductKizaiCreated() {
        return getConductKizaiCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isConductKizaiDeleted() {
        return "conduct-kizai-deleted".equals(kind);
    }

    public Optional<ConductKizaiDeleted> getConductKizaiDeleted() {
        if (isConductKizaiDeleted())
            try {
                return Optional.of(mapper.readValue(body, ConductKizaiDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ConductKizaiDeleted asConductKizaiDeleted() {
        return getConductKizaiDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isConductKizaiUpdated() {
        return "conduct-kizai-updated".equals(kind);
    }

    public Optional<ConductKizaiUpdated> getConductKizaiUpdated() {
        if (isConductKizaiUpdated())
            try {
                return Optional.of(mapper.readValue(body, ConductKizaiUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ConductKizaiUpdated asConductKizaiUpdated() {
        return getConductKizaiUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isConductShinryouCreated() {
        return "conduct-shinryou-created".equals(kind);
    }

    public Optional<ConductShinryouCreated> getConductShinryouCreated() {
        if (isConductShinryouCreated())
            try {
                return Optional.of(mapper.readValue(body, ConductShinryouCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ConductShinryouCreated asConductShinryouCreated() {
        return getConductShinryouCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isConductShinryouDeleted() {
        return "conduct-shinryou-deleted".equals(kind);
    }

    public Optional<ConductShinryouDeleted> getConductShinryouDeleted() {
        if (isConductShinryouDeleted())
            try {
                return Optional.of(mapper.readValue(body, ConductShinryouDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ConductShinryouDeleted asConductShinryouDeleted() {
        return getConductShinryouDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isConductShinryouUpdated() {
        return "conduct-shinryou-updated".equals(kind);
    }

    public Optional<ConductShinryouUpdated> getConductShinryouUpdated() {
        if (isConductShinryouUpdated())
            try {
                return Optional.of(mapper.readValue(body, ConductShinryouUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ConductShinryouUpdated asConductShinryouUpdated() {
        return getConductShinryouUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isConductUpdated() {
        return "conduct-updated".equals(kind);
    }

    public Optional<ConductUpdated> getConductUpdated() {
        if (isConductUpdated())
            try {
                return Optional.of(mapper.readValue(body, ConductUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ConductUpdated asConductUpdated() {
        return getConductUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isDiseaseAdjCreated() {
        return "disease-adj-created".equals(kind);
    }

    public Optional<DiseaseAdjCreated> getDiseaseAdjCreated() {
        if (isDiseaseAdjCreated())
            try {
                return Optional.of(mapper.readValue(body, DiseaseAdjCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public DiseaseAdjCreated asDiseaseAdjCreated() {
        return getDiseaseAdjCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isDiseaseAdjDeleted() {
        return "disease-adj-deleted".equals(kind);
    }

    public Optional<DiseaseAdjDeleted> getDiseaseAdjDeleted() {
        if (isDiseaseAdjDeleted())
            try {
                return Optional.of(mapper.readValue(body, DiseaseAdjDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public DiseaseAdjDeleted asDiseaseAdjDeleted() {
        return getDiseaseAdjDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isDiseaseAdjUpdated() {
        return "disease-adj-updated".equals(kind);
    }

    public Optional<DiseaseAdjUpdated> getDiseaseAdjUpdated() {
        if (isDiseaseAdjUpdated())
            try {
                return Optional.of(mapper.readValue(body, DiseaseAdjUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public DiseaseAdjUpdated asDiseaseAdjUpdated() {
        return getDiseaseAdjUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isDiseaseCreated() {
        return "disease-created".equals(kind);
    }

    public Optional<DiseaseCreated> getDiseaseCreated() {
        if (isDiseaseCreated())
            try {
                return Optional.of(mapper.readValue(body, DiseaseCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public DiseaseCreated asDiseaseCreated() {
        return getDiseaseCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isDiseaseDeleted() {
        return "disease-deleted".equals(kind);
    }

    public Optional<DiseaseDeleted> getDiseaseDeleted() {
        if (isDiseaseDeleted())
            try {
                return Optional.of(mapper.readValue(body, DiseaseDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public DiseaseDeleted asDiseaseDeleted() {
        return getDiseaseDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isDiseaseUpdated() {
        return "disease-updated".equals(kind);
    }

    public Optional<DiseaseUpdated> getDiseaseUpdated() {
        if (isDiseaseUpdated())
            try {
                return Optional.of(mapper.readValue(body, DiseaseUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public DiseaseUpdated asDiseaseUpdated() {
        return getDiseaseUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isDrugCreated() {
        return "drug-created".equals(kind);
    }

    public Optional<DrugCreated> getDrugCreated() {
        if (isDrugCreated())
            try {
                return Optional.of(mapper.readValue(body, DrugCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public DrugCreated asDrugCreated() {
        return getDrugCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isDrugDeleted() {
        return "drug-deleted".equals(kind);
    }

    public Optional<DrugDeleted> getDrugDeleted() {
        if (isDrugDeleted())
            try {
                return Optional.of(mapper.readValue(body, DrugDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public DrugDeleted asDrugDeleted() {
        return getDrugDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isDrugUpdated() {
        return "drug-updated".equals(kind);
    }

    public Optional<DrugUpdated> getDrugUpdated() {
        if (isDrugUpdated())
            try {
                return Optional.of(mapper.readValue(body, DrugUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public DrugUpdated asDrugUpdated() {
        return getDrugUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isGazouLabelCreated() {
        return "gazou-label-created".equals(kind);
    }

    public Optional<GazouLabelCreated> getGazouLabelCreated() {
        if (isGazouLabelCreated())
            try {
                return Optional.of(mapper.readValue(body, GazouLabelCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public GazouLabelCreated asGazouLabelCreated() {
        return getGazouLabelCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isGazouLabelDeleted() {
        return "gazou-label-deleted".equals(kind);
    }

    public Optional<GazouLabelDeleted> getGazouLabelDeleted() {
        if (isGazouLabelDeleted())
            try {
                return Optional.of(mapper.readValue(body, GazouLabelDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public GazouLabelDeleted asGazouLabelDeleted() {
        return getGazouLabelDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isGazouLabelUpdated() {
        return "gazou-label-updated".equals(kind);
    }

    public Optional<GazouLabelUpdated> getGazouLabelUpdated() {
        if (isGazouLabelUpdated())
            try {
                return Optional.of(mapper.readValue(body, GazouLabelUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public GazouLabelUpdated asGazouLabelUpdated() {
        return getGazouLabelUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isKouhiCreated() {
        return "kouhi-created".equals(kind);
    }

    public Optional<KouhiCreated> getKouhiCreated() {
        if (isKouhiCreated())
            try {
                return Optional.of(mapper.readValue(body, KouhiCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public KouhiCreated asKouhiCreated() {
        return getKouhiCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isKouhiDeleted() {
        return "kouhi-deleted".equals(kind);
    }

    public Optional<KouhiDeleted> getKouhiDeleted() {
        if (isKouhiDeleted())
            try {
                return Optional.of(mapper.readValue(body, KouhiDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public KouhiDeleted asKouhiDeleted() {
        return getKouhiDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isKouhiUpdated() {
        return "kouhi-updated".equals(kind);
    }

    public Optional<KouhiUpdated> getKouhiUpdated() {
        if (isKouhiUpdated())
            try {
                return Optional.of(mapper.readValue(body, KouhiUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public KouhiUpdated asKouhiUpdated() {
        return getKouhiUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isKoukikoureiCreated() {
        return "koukikourei-created".equals(kind);
    }

    public Optional<KoukikoureiCreated> getKoukikoureiCreated() {
        if (isKoukikoureiCreated())
            try {
                return Optional.of(mapper.readValue(body, KoukikoureiCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public KoukikoureiCreated asKoukikoureiCreated() {
        return getKoukikoureiCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isKoukikoureiDeleted() {
        return "koukikourei-deleted".equals(kind);
    }

    public Optional<KoukikoureiDeleted> getKoukikoureiDeleted() {
        if (isKoukikoureiDeleted())
            try {
                return Optional.of(mapper.readValue(body, KoukikoureiDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public KoukikoureiDeleted asKoukikoureiDeleted() {
        return getKoukikoureiDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isKoukikoureiUpdated() {
        return "koukikourei-updated".equals(kind);
    }

    public Optional<KoukikoureiUpdated> getKoukikoureiUpdated() {
        if (isKoukikoureiUpdated())
            try {
                return Optional.of(mapper.readValue(body, KoukikoureiUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public KoukikoureiUpdated asKoukikoureiUpdated() {
        return getKoukikoureiUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isPatientCreated() {
        return "patient-created".equals(kind);
    }

    public Optional<PatientCreated> getPatientCreated() {
        if (isPatientCreated())
            try {
                return Optional.of(mapper.readValue(body, PatientCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public PatientCreated asPatientCreated() {
        return getPatientCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isPatientDeleted() {
        return "patient-deleted".equals(kind);
    }

    public Optional<PatientDeleted> getPatientDeleted() {
        if (isPatientDeleted())
            try {
                return Optional.of(mapper.readValue(body, PatientDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public PatientDeleted asPatientDeleted() {
        return getPatientDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isPatientUpdated() {
        return "patient-updated".equals(kind);
    }

    public Optional<PatientUpdated> getPatientUpdated() {
        if (isPatientUpdated())
            try {
                return Optional.of(mapper.readValue(body, PatientUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public PatientUpdated asPatientUpdated() {
        return getPatientUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isPaymentCreated() {
        return "payment-created".equals(kind);
    }

    public Optional<PaymentCreated> getPaymentCreated() {
        if (isPaymentCreated())
            try {
                return Optional.of(mapper.readValue(body, PaymentCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public PaymentCreated asPaymentCreated() {
        return getPaymentCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isPaymentDeleted() {
        return "payment-deleted".equals(kind);
    }

    public Optional<PaymentDeleted> getPaymentDeleted() {
        if (isPaymentDeleted())
            try {
                return Optional.of(mapper.readValue(body, PaymentDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public PaymentDeleted asPaymentDeleted() {
        return getPaymentDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isPaymentUpdated() {
        return "payment-updated".equals(kind);
    }

    public Optional<PaymentUpdated> getPaymentUpdated() {
        if (isPaymentUpdated())
            try {
                return Optional.of(mapper.readValue(body, PaymentUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public PaymentUpdated asPaymentUpdated() {
        return getPaymentUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isPharmaDrugCreated() {
        return "pharma-drug-created".equals(kind);
    }

    public Optional<PharmaDrugCreated> getPharmaDrugCreated() {
        if (isPharmaDrugCreated())
            try {
                return Optional.of(mapper.readValue(body, PharmaDrugCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public PharmaDrugCreated asPharmaDrugCreated() {
        return getPharmaDrugCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isPharmaDrugDeleted() {
        return "pharma-drug-deleted".equals(kind);
    }

    public Optional<PharmaDrugDeleted> getPharmaDrugDeleted() {
        if (isPharmaDrugDeleted())
            try {
                return Optional.of(mapper.readValue(body, PharmaDrugDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public PharmaDrugDeleted asPharmaDrugDeleted() {
        return getPharmaDrugDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isPharmaDrugUpdated() {
        return "pharma-drug-updated".equals(kind);
    }

    public Optional<PharmaDrugUpdated> getPharmaDrugUpdated() {
        if (isPharmaDrugUpdated())
            try {
                return Optional.of(mapper.readValue(body, PharmaDrugUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public PharmaDrugUpdated asPharmaDrugUpdated() {
        return getPharmaDrugUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isPharmaQueueCreated() {
        return "pharma-queue-created".equals(kind);
    }

    public Optional<PharmaQueueCreated> getPharmaQueueCreated() {
        if (isPharmaQueueCreated())
            try {
                return Optional.of(mapper.readValue(body, PharmaQueueCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public PharmaQueueCreated asPharmaQueueCreated() {
        return getPharmaQueueCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isPharmaQueueDeleted() {
        return "pharma-queue-deleted".equals(kind);
    }

    public Optional<PharmaQueueDeleted> getPharmaQueueDeleted() {
        if (isPharmaQueueDeleted())
            try {
                return Optional.of(mapper.readValue(body, PharmaQueueDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public PharmaQueueDeleted asPharmaQueueDeleted() {
        return getPharmaQueueDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isPharmaQueueUpdated() {
        return "pharma-queue-updated".equals(kind);
    }

    public Optional<PharmaQueueUpdated> getPharmaQueueUpdated() {
        if (isPharmaQueueUpdated())
            try {
                return Optional.of(mapper.readValue(body, PharmaQueueUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public PharmaQueueUpdated asPharmaQueueUpdated() {
        return getPharmaQueueUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isRoujinCreated() {
        return "roujin-created".equals(kind);
    }

    public Optional<RoujinCreated> getRoujinCreated() {
        if (isRoujinCreated())
            try {
                return Optional.of(mapper.readValue(body, RoujinCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public RoujinCreated asRoujinCreated() {
        return getRoujinCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isRoujinDeleted() {
        return "roujin-deleted".equals(kind);
    }

    public Optional<RoujinDeleted> getRoujinDeleted() {
        if (isRoujinDeleted())
            try {
                return Optional.of(mapper.readValue(body, RoujinDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public RoujinDeleted asRoujinDeleted() {
        return getRoujinDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isRoujinUpdated() {
        return "roujin-updated".equals(kind);
    }

    public Optional<RoujinUpdated> getRoujinUpdated() {
        if (isRoujinUpdated())
            try {
                return Optional.of(mapper.readValue(body, RoujinUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public RoujinUpdated asRoujinUpdated() {
        return getRoujinUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isShahokokuhoCreated() {
        return "shahokokuho-created".equals(kind);
    }

    public Optional<ShahokokuhoCreated> getShahokokuhoCreated() {
        if (isShahokokuhoCreated())
            try {
                return Optional.of(mapper.readValue(body, ShahokokuhoCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ShahokokuhoCreated asShahokokuhoCreated() {
        return getShahokokuhoCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isShahokokuhoDeleted() {
        return "shahokokuho-deleted".equals(kind);
    }

    public Optional<ShahokokuhoDeleted> getShahokokuhoDeleted() {
        if (isShahokokuhoDeleted())
            try {
                return Optional.of(mapper.readValue(body, ShahokokuhoDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ShahokokuhoDeleted asShahokokuhoDeleted() {
        return getShahokokuhoDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isShahokokuhoUpdated() {
        return "shahokokuho-updated".equals(kind);
    }

    public Optional<ShahokokuhoUpdated> getShahokokuhoUpdated() {
        if (isShahokokuhoUpdated())
            try {
                return Optional.of(mapper.readValue(body, ShahokokuhoUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ShahokokuhoUpdated asShahokokuhoUpdated() {
        return getShahokokuhoUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isShinryouCreated() {
        return "shinryou-created".equals(kind);
    }

    public Optional<ShinryouCreated> getShinryouCreated() {
        if (isShinryouCreated())
            try {
                return Optional.of(mapper.readValue(body, ShinryouCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ShinryouCreated asShinryouCreated() {
        return getShinryouCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isShinryouDeleted() {
        return "shinryou-deleted".equals(kind);
    }

    public Optional<ShinryouDeleted> getShinryouDeleted() {
        if (isShinryouDeleted())
            try {
                return Optional.of(mapper.readValue(body, ShinryouDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ShinryouDeleted asShinryouDeleted() {
        return getShinryouDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isShinryouUpdated() {
        return "shinryou-updated".equals(kind);
    }

    public Optional<ShinryouUpdated> getShinryouUpdated() {
        if (isShinryouUpdated())
            try {
                return Optional.of(mapper.readValue(body, ShinryouUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public ShinryouUpdated asShinryouUpdated() {
        return getShinryouUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isTextCreated() {
        return "text-created".equals(kind);
    }

    public Optional<TextCreated> getTextCreated() {
        if (isTextCreated())
            try {
                return Optional.of(mapper.readValue(body, TextCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public TextCreated asTextCreated() {
        return getTextCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isTextDeleted() {
        return "text-deleted".equals(kind);
    }

    public Optional<TextDeleted> getTextDeleted() {
        if (isTextDeleted())
            try {
                return Optional.of(mapper.readValue(body, TextDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public TextDeleted asTextDeleted() {
        return getTextDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isTextUpdated() {
        return "text-updated".equals(kind);
    }

    public Optional<TextUpdated> getTextUpdated() {
        if (isTextUpdated())
            try {
                return Optional.of(mapper.readValue(body, TextUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public TextUpdated asTextUpdated() {
        return getTextUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isVisitCreated() {
        return "visit-created".equals(kind);
    }

    public Optional<VisitCreated> getVisitCreated() {
        if (isVisitCreated())
            try {
                return Optional.of(mapper.readValue(body, VisitCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public VisitCreated asVisitCreated() {
        return getVisitCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isVisitDeleted() {
        return "visit-deleted".equals(kind);
    }

    public Optional<VisitDeleted> getVisitDeleted() {
        if (isVisitDeleted())
            try {
                return Optional.of(mapper.readValue(body, VisitDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public VisitDeleted asVisitDeleted() {
        return getVisitDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isVisitUpdated() {
        return "visit-updated".equals(kind);
    }

    public Optional<VisitUpdated> getVisitUpdated() {
        if (isVisitUpdated())
            try {
                return Optional.of(mapper.readValue(body, VisitUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public VisitUpdated asVisitUpdated() {
        return getVisitUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isWqueueCreated() {
        return "wqueue-created".equals(kind);
    }

    public Optional<WqueueCreated> getWqueueCreated() {
        if (isWqueueCreated())
            try {
                return Optional.of(mapper.readValue(body, WqueueCreated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public WqueueCreated asWqueueCreated() {
        return getWqueueCreated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isWqueueDeleted() {
        return "wqueue-deleted".equals(kind);
    }

    public Optional<WqueueDeleted> getWqueueDeleted() {
        if (isWqueueDeleted())
            try {
                return Optional.of(mapper.readValue(body, WqueueDeleted.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public WqueueDeleted asWqueueDeleted() {
        return getWqueueDeleted().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }

    public boolean isWqueueUpdated() {
        return "wqueue-updated".equals(kind);
    }

    public Optional<WqueueUpdated> getWqueueUpdated() {
        if (isWqueueUpdated())
            try {
                return Optional.of(mapper.readValue(body, WqueueUpdated.class));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        else
            return Optional.empty();
    }

    public WqueueUpdated asWqueueUpdated() {
        return getWqueueUpdated().orElseThrow(() -> new RuntimeException("Invalid practice-log kind."));
    }
}
