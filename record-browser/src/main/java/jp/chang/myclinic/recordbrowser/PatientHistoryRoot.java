package jp.chang.myclinic.recordbrowser;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.utilfx.Nav;

import java.util.List;

class PatientHistoryRoot extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(PatientHistoryRoot.class);

    private Label mainLabel = new Label("");
    private RecordListByPatient recordList = new RecordListByPatient();
    private ByPatientNavHandler navHandler;
    private Nav nav = new Nav();

    PatientHistoryRoot(PatientDTO patient) {
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        ScrollPane recordScroll = new ScrollPane(recordList);
        recordScroll.getStyleClass().add("record-scroll");
        recordScroll.setFitToWidth(true);
        navHandler = new ByPatientNavHandler(patient);
        navHandler.setPageCallback(this::pageCallback);
        nav.setHandler(navHandler);
        updateMainLabel(patient);
        getChildren().addAll(
                mainLabel,
                nav,
                recordScroll
        );
    }

    void trigger(){
        nav.trigger();
    }

    private void pageCallback(List<VisitFull2DTO> visits){
        recordList.clear();
        visits.forEach(recordList::add);
    }

    private void updateMainLabel(PatientDTO patient){
        mainLabel.setText(createMainLabelText(patient));
    }

    private String createMainLabelText(PatientDTO patient){
        return String.format("(%d) %s %s (%s %s)", patient.patientId,
                patient.lastName, patient.firstName, patient.lastNameYomi, patient.firstNameYomi);
    }


}
