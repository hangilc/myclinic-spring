package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.backend.persistence.DrugPersistence;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class DrugPersistenceMock implements DrugPersistence {

    private Map<Integer, DrugAttrDTO> attrs = new HashMap<>();

    @Override
    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        return drugIds.stream().map(id -> attrs.getOrDefault(id, null))
                .filter(Objects::nonNull).collect(toList());
    }
}
