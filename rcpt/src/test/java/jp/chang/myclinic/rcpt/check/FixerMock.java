package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseNewDTO;
import jp.chang.myclinic.dto.ShinryouDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class FixerMock implements Fixer {

    private static Logger logger = LoggerFactory.getLogger(FixerMock.class);

    FixerMock() {

    }

    @Override
    public int enterShinryou(ShinryouDTO shinryou) {
        return 0;
    }

    @Override
    public boolean batchDeleteShinryou(List<Integer> shinryouIds) {
        return false;
    }

    @Override
    public int enterDisease(DiseaseNewDTO disease) {
        return 0;
    }
}
