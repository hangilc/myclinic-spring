package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.ShahokokuhoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public interface ShahokokuhoRepoInterface {

    List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at);
}
