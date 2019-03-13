package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.dto.RoujinDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RoujinPersistenceMysql {

    public RoujinDTO getRoujin(int roujinId) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
