package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.backend.persistence.PracticeLogPersistence;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PracticeLogPersistenceMock implements PracticeLogPersistence {

    private int serialId = 1;
    private List<PracticeLogDTO> logs = new ArrayList<>();

    @Override
    public void enterPracticeLog(PracticeLogDTO dto) {
        dto.serialId = serialId++;
        logs.add(dto);
    }

    @Override
    public Optional<PracticeLogDTO> findLastPracticeLog(LocalDate at) {
        if( logs.size() > 0 ){
            PracticeLogDTO log = logs.get(logs.size()-1);
            LocalDate createdAt = DateTimeUtil.parseSqlDateTime(log.createdAt).toLocalDate();
            if( at.equals(createdAt) ){
                return Optional.of(log);
            }
        }
        return Optional.empty();
    }

}
