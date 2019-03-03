package jp.chang.myclinic.backend.persistence;

import jp.chang.myclinic.dto.ShinryouAttrDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface ShinryouPersistence {

    List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds);

}
