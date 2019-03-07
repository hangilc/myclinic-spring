package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.WqueuePersistence;
import jp.chang.myclinic.dto.*;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class WqueuePersistenceMysql implements WqueuePersistence {

    @Override
    public void enterWqueue(WqueueDTO wqueue) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
