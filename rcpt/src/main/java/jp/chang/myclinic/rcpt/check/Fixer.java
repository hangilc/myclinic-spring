package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseNewDTO;
import jp.chang.myclinic.dto.ShinryouDTO;

import java.util.List;

public interface Fixer {

    int enterShinryou(ShinryouDTO shinryou);
    boolean batchDeleteShinryou(List<Integer> shinryouIds);
    int enterDisease(DiseaseNewDTO disease);

}
