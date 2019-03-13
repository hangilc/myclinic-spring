package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.backendmock.util.Helper;
import jp.chang.myclinic.dto.RoujinDTO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class RoujinPersistenceMock {

    private Map<Integer, RoujinDTO> registry = new HashMap<>();
    private Helper helper = new Helper();

    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at) {
        return registry.values().stream().filter(
                dto -> dto.patientId == patientId &&
                        helper.isValidAt(dto.validFrom, dto.validUpto, at.toString())
        ).collect(toList());
    }

    public RoujinDTO getRoujin(int roujinId) {
        return registry.get(roujinId);
    }

}
