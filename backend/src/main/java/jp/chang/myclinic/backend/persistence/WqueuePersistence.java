package jp.chang.myclinic.backend.persistence;

import jp.chang.myclinic.dto.WqueueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface WqueuePersistence {

    void enterWqueue(WqueueDTO wqueue);

}
