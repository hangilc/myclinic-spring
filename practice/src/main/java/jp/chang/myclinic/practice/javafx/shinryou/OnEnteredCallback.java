package jp.chang.myclinic.practice.javafx.shinryou;

import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public interface OnEnteredCallback {
    void accept(List<ShinryouFullDTO> shinryouList, Map<Integer, ShinryouAttrDTO> attrMap,
                List<ConductFullDTO> conducts);
}
