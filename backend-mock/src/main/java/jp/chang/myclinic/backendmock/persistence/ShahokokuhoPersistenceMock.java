package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.backend.persistence.ShahokokuhoPersistence;
import jp.chang.myclinic.backendmock.util.Helper;
import jp.chang.myclinic.dto.ShahokokuhoDTO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ShahokokuhoPersistenceMock implements ShahokokuhoPersistence {

    private Map<Integer, ShahokokuhoDTO> registry = new HashMap<>();
    private Helper helper = new Helper();

    @Override
    public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at) {
        return registry.values().stream().filter(
                dto -> dto.patientId == patientId &&
                        helper.isValidAt(dto.validFrom, dto.validUpto, at.toString())
        ).collect(toList());
    }

    @Override
    public ShahokokuhoDTO getShahokokuho(int shahokokuhoId) {
        return registry.get(shahokokuhoId);
    }

}
