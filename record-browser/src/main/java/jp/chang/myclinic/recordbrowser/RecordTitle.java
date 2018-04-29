package jp.chang.myclinic.recordbrowser;

import javafx.scene.control.Label;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.util.DateTimeUtil;

class RecordTitle extends Label {

    //private static Logger logger = LoggerFactory.getLogger(RecordTitle.class);

    RecordTitle(PatientDTO patient, VisitDTO visit) {
        this();
        String s = String.format("(%d) %s%s %s", patient.patientId, patient.lastName, patient.firstName,
                dateTimeString(visit.visitedAt));
        setText(s);
    }

    RecordTitle(VisitDTO visit){
        this();
        String s = dateTimeString(visit.visitedAt);
        setText(s);
    }

    private RecordTitle(){
        getStyleClass().add("record-title");
        setMaxWidth(Double.MAX_VALUE);
    }

    private String dateTimeString(String at){
        return DateTimeUtil.sqlDateTimeToKanji(at,
                DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4);
    }

}
