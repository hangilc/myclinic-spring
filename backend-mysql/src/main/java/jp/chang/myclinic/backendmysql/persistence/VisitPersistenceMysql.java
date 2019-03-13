package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.dto.ShoukiDTO;
import jp.chang.myclinic.dto.VisitDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VisitPersistenceMysql {

    public int enterVisit(VisitDTO visit) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    public VisitDTO getVisit(int visitId) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
