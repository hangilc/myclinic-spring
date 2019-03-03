package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.DrugAttrDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface DrugRepoInterface {

    List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds);

}
