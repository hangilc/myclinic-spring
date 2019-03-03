package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.WqueueDTO;

import java.util.HashMap;
import java.util.Map;

public class WqueueRepo implements WqueueRepoInterface {

    private Map<Integer, WqueueDTO> registry = new HashMap<>();

    @Override
    public void enterWqueue(WqueueDTO wqueue) {
        registry.put(wqueue.visitId, wqueue);
    }
}
