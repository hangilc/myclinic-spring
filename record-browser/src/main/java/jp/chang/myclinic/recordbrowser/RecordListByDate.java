package jp.chang.myclinic.recordbrowser;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.dto.VisitFull2PatientDTO;
import jp.chang.myclinic.util.DateTimeUtil;

class RecordListByDate extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(RecordListByDate.class);

    RecordListByDate(){
        super(4);
    }

    void add(VisitFull2PatientDTO data){
        getChildren().add(new VBox(
                createTitle(data),
                new RecordBody(data.visitFull)
        ));
    }

    void clear(){
        getChildren().clear();
    }

    private Node createTitle(VisitFull2PatientDTO data) {
        PatientDTO patient = data.patient;
        VisitFull2DTO visit = data.visitFull;
        String patientText = String.format("(%d) %s%s", patient.patientId, patient.lastName, patient.firstName);
        String dateText = dateTimeString(visit.visit.visitedAt);
        Hyperlink patientLink = new Hyperlink(patientText);
        patientLink.setOnAction(evt -> {
            PatientHistoryDialog dialog = new PatientHistoryDialog(patient);
            Main.setAsChildWindow(dialog);
            dialog.setX(Main.getXofMainStage() + 40);
            dialog.setY(Main.getYofMainStage() + 20);
            dialog.show();
        });
        patientLink.getStyleClass().add("patient-link");
        TextFlow textFlow = new TextFlow(patientLink, new Label(" : "), new Label(dateText));
        textFlow.getStyleClass().add("record-title");
        return textFlow;
    }

    private String dateTimeString(String at){
        return DateTimeUtil.sqlDateTimeToKanji(at,
                DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4);
    }

}
