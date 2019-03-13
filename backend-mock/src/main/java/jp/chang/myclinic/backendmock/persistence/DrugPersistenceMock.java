package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.dto.DrugAttrDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class DrugPersistenceMock {

    private Map<Integer, DrugAttrDTO> attrs = new HashMap<>();

    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        return drugIds.stream().map(id -> attrs.getOrDefault(id, null))
                .filter(Objects::nonNull).collect(toList());
    }
}
