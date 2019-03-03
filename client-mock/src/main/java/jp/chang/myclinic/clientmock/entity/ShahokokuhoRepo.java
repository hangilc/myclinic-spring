package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.ShahokokuhoDTO;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ShahokokuhoRepo implements ShahokokuhoRepoInterface {

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
