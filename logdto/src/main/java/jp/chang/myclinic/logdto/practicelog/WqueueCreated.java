package jp.chang.myclinic.logdto.practicelog;

import jp.chang.myclinic.dto.WqueueDTO;

public class WqueueCreated implements PracticeLogBody {

    public WqueueDTO wqueue;

    public WqueueCreated(WqueueDTO wqueue) {
        this.wqueue = wqueue;
    }
}
