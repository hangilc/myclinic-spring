package jp.chang.myclinic.backend.persistence;

import jp.chang.myclinic.dto.KoukikoureiDTO;

import java.time.LocalDate;
import java.util.List;

public interface KoukikoureiPersistence {
    List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at);
    KoukikoureiDTO getKoukikourei(int koukikoureiId);
}
