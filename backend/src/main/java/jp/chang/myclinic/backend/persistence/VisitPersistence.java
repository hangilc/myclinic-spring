package jp.chang.myclinic.backend.persistence;

import jp.chang.myclinic.dto.ShoukiDTO;
import jp.chang.myclinic.dto.VisitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface VisitPersistence {

    int enterVisit(VisitDTO visit);

    List<ShoukiDTO> batchGetShouki(List<Integer> visitIds);

    VisitDTO getVisit(int visitId);
}
