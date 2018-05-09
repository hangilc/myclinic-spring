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
    private List<String> errorMessages = new ArrayList<>();
    private List<String> fixMessages = new ArrayList<>();

    List<ShinryouDTO> getEnteredShinryouList() {
        return enteredShinryouList;
    }

    List<List<Integer>> getBatchDeletedShinryouList() {
        return batchDeletedShinryouList;
    }

    List<DiseaseNewDTO> getEnteredDisseases() {
        return enteredDisseases;
    }

    List<String> getErrorMessages() {
        return errorMessages;
    }

    List<String> getFixMessages() {
        return fixMessages;
    }
}
