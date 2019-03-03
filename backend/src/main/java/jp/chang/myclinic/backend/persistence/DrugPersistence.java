package jp.chang.myclinic.backend.persistence;

import jp.chang.myclinic.dto.DrugAttrDTO;

import java.util.List;

public interface DrugPersistence {
    List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds);
}
