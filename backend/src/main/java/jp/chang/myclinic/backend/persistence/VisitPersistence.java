package jp.chang.myclinic.backend.persistence;

import jp.chang.myclinic.dto.VisitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface VisitPersistence {

    int enterVisit(VisitDTO visit);

}
