package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.backend.persistence.KouhiPersistence;
import jp.chang.myclinic.dto.KouhiDTO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class KouhiPersistenceMock implements KouhiPersistence {

    private Map<Integer, KouhiDTO> registry = new HashMap<>();
    private Helper helper = new Helper();

    @Override
    public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at) {
        return registry.values().stream().filter(
                dto -> dto.patientId == patientId &&
                        helper.isValidAt(dto.validFrom, dto.validUpto, at.toString())
        ).collect(toList());
    }

    @Override
    public KouhiDTO getKouhi(int kouhiId) {
        return registry.get(kouhiId);
    }

}
