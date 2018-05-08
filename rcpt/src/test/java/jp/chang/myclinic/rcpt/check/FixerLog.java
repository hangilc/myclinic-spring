package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseNewDTO;
import jp.chang.myclinic.dto.ShinryouDTO;

import java.util.ArrayList;
import java.util.List;

class FixerLog {

    //private static Logger logger = LoggerFactory.getLogger(FixerLog.class);
    private List<ShinryouDTO> enteredShinryouList = new ArrayList<>();
    private List<List<Integer>> batchDeletedShinryouList = new ArrayList<>();
    private List<DiseaseNewDTO> enteredDisseases = new ArrayList<>();

    public List<ShinryouDTO> getEnteredShinryouList() {
        return enteredShinryouList;
    }

    public List<List<Integer>> getBatchDeletedShinryouList() {
        return batchDeletedShinryouList;
    }

    public List<DiseaseNewDTO> getEnteredDisseases() {
        return enteredDisseases;
    }
}
