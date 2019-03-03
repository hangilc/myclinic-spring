package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.backend.persistence.VisitPersistence;
import jp.chang.myclinic.dto.ShoukiDTO;
import jp.chang.myclinic.dto.VisitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class VisitPersistenceMock implements VisitPersistence {

    private Map<Integer, VisitDTO> registry = new HashMap<>();
    private Map<Integer, ShoukiDTO> shoukiRegistry = new HashMap<>();
    private int serialId = 1;

    @Override
    public int enterVisit(VisitDTO visit) {
        int visitId = serialId++;
        visit.visitId = visitId;
        registry.put(visitId, visit);
        return visitId;
    }

    @Override
    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds) {
        return visitIds.stream().map(id -> shoukiRegistry.getOrDefault(id, null))
                .filter(Objects::nonNull).collect(toList());
    }

    @Override
    public VisitDTO getVisit(int visitId) {
        return registry.get(visitId);
    }

}
