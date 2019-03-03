package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.WqueueDTO;

public interface WqueueRepoInterface {
    void enterWqueue(WqueueDTO wqueue);
}
