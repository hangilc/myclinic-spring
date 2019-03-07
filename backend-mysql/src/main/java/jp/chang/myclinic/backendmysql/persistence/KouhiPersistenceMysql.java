package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.KouhiPersistence;
import jp.chang.myclinic.dto.*;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class KouhiPersistenceMysql implements KouhiPersistence {

    @Override
    public KouhiDTO getKouhi(int kouhiId) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate atDate) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
