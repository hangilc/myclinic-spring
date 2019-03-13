package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.backendmock.util.Helper;
import jp.chang.myclinic.dto.KoukikoureiDTO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class KoukikoureiPersistenceMock {

    private Map<Integer, KoukikoureiDTO> registry = new HashMap<>();
    private Helper helper = new Helper();

    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at) {
        return registry.values().stream().filter(
                dto -> dto.patientId == patientId &&
                        helper.isValidAt(dto.validFrom, dto.validUpto, at.toString())
        ).collect(toList());
    }

    public KoukikoureiDTO getKoukikourei(int koukikoureiId) {
        return registry.get(koukikoureiId);
    }

}
