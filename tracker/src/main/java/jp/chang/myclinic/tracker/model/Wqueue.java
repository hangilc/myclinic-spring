package jp.chang.myclinic.tracker.model;

import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.WqueueDTO;

public class Wqueue {

    private ModelRegistry registry;
    private int visitId;
    private WqueueWaitState waitState;

    Wqueue(ModelRegistry registry, WqueueDTO dto) {
        this.registry = registry;
        this.visitId = dto.visitId;
        this.waitState = WqueueWaitState.fromCode(dto.waitState);
    }

    public int getVisitId() {
        return visitId;
    }

    public Visit getVisit(){
        return registry.getVisit(visitId);
    }

    public void update(WqueueDTO dto){
        this.waitState = WqueueWaitState.fromCode(dto.waitState);
    }

}
