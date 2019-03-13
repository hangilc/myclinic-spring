package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.dto.ShinryouAttrDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ShinryouPersistenceMock {

    private Map<Integer, ShinryouAttrDTO> attrs = new HashMap<>();

    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return shinryouIds.stream().map(id -> attrs.getOrDefault(id, null))
                .filter(Objects::nonNull).collect(toList());
    }
}
