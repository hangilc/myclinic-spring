package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.backend.persistence.WqueuePersistence;
import jp.chang.myclinic.dto.WqueueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WqueuePersistenceMock implements WqueuePersistence {

    private Map<Integer, WqueueDTO> registry = new HashMap<>();

    @Override
    public void enterWqueue(WqueueDTO wqueue) {
        registry.put(wqueue.visitId, wqueue);
    }

}
