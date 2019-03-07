package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.KoukikoureiPersistence;
import jp.chang.myclinic.backendmysql.entity.core.DTOMapper;
import jp.chang.myclinic.backendmysql.entity.core.Koukikourei;
import jp.chang.myclinic.backendmysql.entity.core.KoukikoureiRepository;
import jp.chang.myclinic.dto.KoukikoureiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KoukikoureiPersistenceMysql implements KoukikoureiPersistence {

    @Autowired
    private KoukikoureiRepository koukikoureiRepository;
    @Autowired
    private DTOMapper mapper;

    @Override
    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at) {
        Sort sort = Sort.by(Sort.Direction.DESC, "koukikoureiId");
        String atDate = at.toString();
        try (Stream<Koukikourei> stream = koukikoureiRepository.findAvailable(patientId, atDate, sort)) {
            return stream.map(mapper::toKoukikoureiDTO).collect(Collectors.toList());
        }
    }

    @Override
    public KoukikoureiDTO getKoukikourei(int koukikoureiId) {
        return mapper.toKoukikoureiDTO(koukikoureiRepository.findById(koukikoureiId));
    }

}
