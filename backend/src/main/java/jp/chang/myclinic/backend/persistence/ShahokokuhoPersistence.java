package jp.chang.myclinic.backend.persistence;

import jp.chang.myclinic.dto.ShahokokuhoDTO;

import java.time.LocalDate;
import java.util.List;

public interface ShahokokuhoPersistence {
    List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at);
    ShahokokuhoDTO getShahokokuho(int shahokokuhoId);
}
