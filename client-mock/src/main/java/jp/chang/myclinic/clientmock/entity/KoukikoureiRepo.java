package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.KoukikoureiDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class KoukikoureiRepo implements KoukikoureiRepoInterface {

    private Map<Integer, List<KoukikoureiDTO>> registry = new HashMap<>();
    private Helper helper = new Helper();

    @Override
    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at) {
        return registry.getOrDefault(patientId, Collections.emptyList()).stream()
                .filter(dto -> helper.isValidAt(dto.validFrom, dto.validUpto, at.toString()))
                .collect(toList());
    }


}
