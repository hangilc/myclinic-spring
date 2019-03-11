package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.ShinryouPersistence;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShinryouPersistenceMysql implements ShinryouPersistence {

    @Override
    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
