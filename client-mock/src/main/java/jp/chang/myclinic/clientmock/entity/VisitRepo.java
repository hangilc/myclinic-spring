package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.VisitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class VisitRepo implements VisitRepoInterface {

    private Map<Integer, VisitDTO> registry = new HashMap<>();
    private int serialId = 1;

    @Override
    public int enterVisit(VisitDTO visit) {
        int visitId = serialId++;
        visit.visitId = visitId;
        registry.put(visitId, visit);
        return visitId;
    }
}
