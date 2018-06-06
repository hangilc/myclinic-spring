package jp.chang.myclinic.logdto.practicelog;

import jp.chang.myclinic.dto.RoujinDTO;

public class RoujinCreated implements PracticeLogBody {

    public RoujinDTO created;

    public RoujinCreated(RoujinDTO created) {
        this.created = created;
    }
}