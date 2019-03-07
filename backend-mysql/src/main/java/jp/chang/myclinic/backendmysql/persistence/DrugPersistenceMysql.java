package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.DrugPersistence;
import jp.chang.myclinic.backendmysql.entity.core.DTOMapper;
import jp.chang.myclinic.backendmysql.entity.core.DrugAttrRepository;
import jp.chang.myclinic.dto.DrugAttrDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DrugPersistenceMysql implements DrugPersistence {

    @Autowired
    private DrugAttrRepository drugAttrRepository;
    @Autowired
    private DTOMapper mapper;

    @Override
    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        if( drugIds.size() == 0 ){
            return Collections.emptyList();
        } else {
            return drugAttrRepository.batchGetDrugAttr(drugIds).stream()
                    .map(mapper::toDrugAttrDTO).collect(Collectors.toList());
        }
    }
}
