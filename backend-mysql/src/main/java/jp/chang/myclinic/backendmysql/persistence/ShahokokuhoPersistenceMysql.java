package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.ShahokokuhoPersistence;
import jp.chang.myclinic.dto.*;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ShahokokuhoPersistenceMysql implements ShahokokuhoPersistence {

    @Override
    public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public ShahokokuhoDTO getShahokokuho(int shahokokuhoId) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
