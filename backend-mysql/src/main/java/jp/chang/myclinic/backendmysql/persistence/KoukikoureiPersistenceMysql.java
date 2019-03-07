package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.KoukikoureiPersistence;
import jp.chang.myclinic.dto.*;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class KoukikoureiPersistenceMysql implements KoukikoureiPersistence {

    @Override
    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public KoukikoureiDTO getKoukikourei(int koukikoureiId) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
