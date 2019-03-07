package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.KouhiPersistence;
import jp.chang.myclinic.backendmysql.entity.core.DTOMapper;
import jp.chang.myclinic.backendmysql.entity.core.Kouhi;
import jp.chang.myclinic.backendmysql.entity.core.KouhiRepository;
import jp.chang.myclinic.dto.KouhiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KouhiPersistenceMysql implements KouhiPersistence {

    @Autowired
    private KouhiRepository kouhiRepository;
    @Autowired
    private DTOMapper mapper;

    @Override
    public KouhiDTO getKouhi(int kouhiId) {
        return mapper.toKouhiDTO(kouhiRepository.findById(kouhiId));
    }

    @Override
    public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at) {
        Sort sort = Sort.by(Sort.Direction.ASC, "kouhiId");
        String atDate = at.toString();
        try (Stream<Kouhi> stream = kouhiRepository.findAvailable(patientId, atDate, sort)) {
            return stream.map(mapper::toKouhiDTO).collect(Collectors.toList());
        }
    }
}
