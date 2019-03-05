package jp.chang.myclinic.backend.persistence;

import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

import java.time.LocalDate;
import java.util.Optional;

public interface PracticeLogPersistence {
    void enterPracticeLog(PracticeLogDTO dto);
    Optional<PracticeLogDTO> findLastPracticeLog(LocalDate at);
}
