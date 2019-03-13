package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.dto.WqueueDTO;

import java.util.HashMap;
import java.util.Map;

public class WqueuePersistenceMock {

    private Map<Integer, WqueueDTO> registry = new HashMap<>();

    public void enterWqueue(WqueueDTO wqueue) {
        registry.put(wqueue.visitId, wqueue);
    }

}
