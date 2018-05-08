package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseNewDTO;
import jp.chang.myclinic.dto.ShinryouDTO;

import java.util.ArrayList;
import java.util.List;

class FixerMock implements Fixer {

    //private static Logger logger = LoggerFactory.getLogger(FixerMock.class);
    private FixerLog fixerLog;

    FixerMock(FixerLog fixerLog) {
        this.fixerLog = fixerLog;
    }

    @Override
    public int enterShinryou(ShinryouDTO shinryou) {
        fixerLog.getEnteredShinryouList().add(shinryou);
        return 0;
    }

    @Override
    public boolean batchDeleteShinryou(List<Integer> shinryouIds) {
        fixerLog.getBatchDeletedShinryouList().add(shinryouIds);
        return false;
    }

    @Override
    public int enterDisease(DiseaseNewDTO disease) {
        fixerLog.getEnteredDisseases().add(disease);
        return 0;
    }

}
