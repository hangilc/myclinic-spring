package jp.chang.myclinic.logdto.practicelog;

import jp.chang.myclinic.dto.ShinryouDTO;

public class ShinryouCreated implements PracticeLogBody {

    public ShinryouDTO created;

    public ShinryouCreated(ShinryouDTO created) {
        this.created = created;
    }
}