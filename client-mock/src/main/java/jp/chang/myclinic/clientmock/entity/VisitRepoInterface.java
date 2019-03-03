package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.ShoukiDTO;
import jp.chang.myclinic.dto.VisitDTO;

import java.util.List;

public interface VisitRepoInterface {
    int enterVisit(VisitDTO visit);

    List<ShoukiDTO> batchGetShouki(List<Integer> visitIds);

    VisitDTO getVisit(int visitId);
}
