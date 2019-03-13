package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backendmysql.entity.core.DTOMapper;
import jp.chang.myclinic.backendmysql.entity.core.Wqueue;
import jp.chang.myclinic.backendmysql.entity.core.WqueueRepository;
import jp.chang.myclinic.dto.WqueueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WqueuePersistenceMysql {

    @Autowired
    private WqueueRepository wqueueRepository;

    @Autowired
    private DTOMapper mapper;

    public void enterWqueue(WqueueDTO wqueueDTO) {
        Wqueue wqueue = mapper.fromWqueueDTO(wqueueDTO);
        wqueueRepository.save(wqueue);
    }
}
