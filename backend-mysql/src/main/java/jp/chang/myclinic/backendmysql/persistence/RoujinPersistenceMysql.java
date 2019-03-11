package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.RoujinPersistence;
import jp.chang.myclinic.dto.RoujinDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RoujinPersistenceMysql implements RoujinPersistence {

    @Override
    public RoujinDTO getRoujin(int roujinId) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
