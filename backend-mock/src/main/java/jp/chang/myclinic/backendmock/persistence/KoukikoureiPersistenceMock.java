package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.backend.persistence.KoukikoureiPersistence;
import jp.chang.myclinic.dto.KoukikoureiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class KoukikoureiPersistenceMock implements KoukikoureiPersistence {

    private Map<Integer, KoukikoureiDTO> registry = new HashMap<>();
    private Helper helper = new Helper();

    @Override
    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at) {
        return registry.values().stream().filter(
                dto -> dto.patientId == patientId &&
                        helper.isValidAt(dto.validFrom, dto.validUpto, at.toString())
        ).collect(toList());
    }

    @Override
    public KoukikoureiDTO getKoukikourei(int koukikoureiId) {
        return registry.get(koukikoureiId);
    }

}
