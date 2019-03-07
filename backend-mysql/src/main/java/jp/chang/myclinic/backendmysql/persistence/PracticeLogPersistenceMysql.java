package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.PracticeLogPersistence;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class PracticeLogPersistenceMysql implements PracticeLogPersistence {

    @Override
    public Optional<PracticeLogDTO> findLastPracticeLog(LocalDate at) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public void enterPracticeLog(PracticeLogDTO dto) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
