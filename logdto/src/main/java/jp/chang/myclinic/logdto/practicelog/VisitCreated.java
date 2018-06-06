package jp.chang.myclinic.logdto.practicelog;

import jp.chang.myclinic.dto.VisitDTO;

public class VisitCreated implements PracticeLogBody {

    public VisitDTO visit;

    public VisitCreated(VisitDTO visit) {
        this.visit = visit;
    }
}
