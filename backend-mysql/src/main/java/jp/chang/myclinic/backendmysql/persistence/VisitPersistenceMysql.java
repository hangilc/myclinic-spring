package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.VisitPersistence;
import jp.chang.myclinic.dto.*;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class VisitPersistenceMysql implements VisitPersistence {

    @Override
    public int enterVisit(VisitDTO visit) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public VisitDTO getVisit(int visitId) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
