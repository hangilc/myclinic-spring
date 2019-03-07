package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.DrugPersistence;
import jp.chang.myclinic.dto.DrugAttrDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DrugPersistenceMysql implements DrugPersistence {

    @Override
    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
