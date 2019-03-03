package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.KouhiDTO;
import jp.chang.myclinic.dto.KoukikoureiDTO;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class KouhiRepo implements KouhiRepoInterface {

    private Map<Integer, List<KouhiDTO>> registry = new HashMap<>();
    private Helper helper = new Helper();

    @Override
    public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at) {
        return registry.getOrDefault(patientId, Collections.emptyList()).stream()
                .filter(dto -> helper.isValidAt(dto.validFrom, dto.validUpto, at.toString()))
                .collect(toList());
    }
}
