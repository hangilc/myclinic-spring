package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.backend.persistence.ShinryouPersistence;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ShinryouPersistenceMock implements ShinryouPersistence {

    private Map<Integer, ShinryouAttrDTO> attrs = new HashMap<>();

    @Override
    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return shinryouIds.stream().map(id -> attrs.getOrDefault(id, null))
                .filter(Objects::nonNull).collect(toList());
    }
}