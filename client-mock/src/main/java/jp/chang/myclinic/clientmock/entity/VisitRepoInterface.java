package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.VisitDTO;

public interface VisitRepoInterface {
    int enterVisit(VisitDTO visit);
}
