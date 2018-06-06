package jp.chang.myclinic.logdto.practicelog;

import jp.chang.myclinic.dto.VisitDTO;

public class HokenUpdated implements PracticeLogBody {

    public VisitDTO prev;
    public VisitDTO updated;

    public HokenUpdated(VisitDTO prev, VisitDTO updated) {
        this.prev = prev;
        this.updated = updated;
    }
}
