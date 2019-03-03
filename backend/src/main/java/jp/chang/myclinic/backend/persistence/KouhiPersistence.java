package jp.chang.myclinic.backend.persistence;

import jp.chang.myclinic.dto.KouhiDTO;

import java.time.LocalDate;
import java.util.List;

public interface KouhiPersistence {
    List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate atDate);
    KouhiDTO getKouhi(int kouhiId);
}
