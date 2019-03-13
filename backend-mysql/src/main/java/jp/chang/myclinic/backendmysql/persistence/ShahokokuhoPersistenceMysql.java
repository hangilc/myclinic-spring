package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.dto.ShahokokuhoDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ShahokokuhoPersistenceMysql {

    public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    public ShahokokuhoDTO getShahokokuho(int shahokokuhoId) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
