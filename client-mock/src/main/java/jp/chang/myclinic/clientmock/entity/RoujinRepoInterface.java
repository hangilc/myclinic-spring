package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.RoujinDTO;

import java.time.LocalDate;
import java.util.List;

public interface RoujinRepoInterface {
    List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at);
}
