package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.backend.persistence.RoujinPersistence;
import jp.chang.myclinic.dto.RoujinDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class RoujinPersistenceMock implements RoujinPersistence {

    private Map<Integer, RoujinDTO> registry = new HashMap<>();
    private Helper helper = new Helper();

    @Override
    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at) {
        return registry.values().stream().filter(
                dto -> dto.patientId == patientId &&
                        helper.isValidAt(dto.validFrom, dto.validUpto, at.toString())
        ).collect(toList());
    }

    @Override
    public RoujinDTO getRoujin(int roujinId) {
        return registry.get(roujinId);
    }

}
