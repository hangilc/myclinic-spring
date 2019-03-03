package jp.chang.myclinic.backend.persistence;

import jp.chang.myclinic.dto.RoujinDTO;

import java.time.LocalDate;
import java.util.List;

public interface RoujinPersistence {
    List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at);
    RoujinDTO getRoujin(int roujinId);
}
