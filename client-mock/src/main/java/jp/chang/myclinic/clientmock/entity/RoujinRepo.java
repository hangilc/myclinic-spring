package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.KoukikoureiDTO;
import jp.chang.myclinic.dto.RoujinDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class RoujinRepo implements RoujinRepoInterface {

    private Map<Integer, List<RoujinDTO>> registry = new HashMap<>();
    private Helper helper = new Helper();

    @Override
    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at) {
        return registry.getOrDefault(patientId, Collections.emptyList()).stream()
                .filter(dto -> helper.isValidAt(dto.validFrom, dto.validUpto, at.toString()))
                .collect(toList());
    }

}
