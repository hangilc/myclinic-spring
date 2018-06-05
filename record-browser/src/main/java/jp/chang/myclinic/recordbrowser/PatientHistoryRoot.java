package jp.chang.myclinic.recordbrowser;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.utilfx.Nav;

import java.time.LocalDate;
import java.util.List;

class PatientHistoryRoot extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(PatientHistoryRoot.class);

    private Label mainLabel = new Label("");
    private RecordListByPatient recordList = new RecordListByPatient();
    private ByPatientNavHandler navHandler;
    private Nav nav = new Nav();

    PatientHistoryRoot(PatientDTO patient) {
        super(2);
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        ScrollPane recordScroll = new ScrollPane(recordList);
        recordScroll.getStyleClass().add("record-scroll");
        recordScroll.setFitToWidth(true);
        getChildren().addAll(
                mainLabel,
                createNavRow(patient),
                recordScroll
        );
        updateMainLabel(patient);
    }

    void trigger(){
        nav.trigger();
    }

    private Node createNavRow(PatientDTO patient){
        HBox hbox = new HBox(4);
        navHandler = new ByPatientNavHandler(patient);
        navHandler.setPageCallback(this::pageCallback);
        nav.setHandler(navHandler);
        mainLabel.setMaxWidth(Double.MAX_VALUE);
        Button refreshButton = new Button("更新");
        refreshButton.setOnAction(evt -> nav.trigger());
        hbox.getChildren().addAll(
                nav,
                refreshButton
        );
        return hbox;
    }

    private void pageCallback(List<VisitFull2DTO> visits){
        recordList.clear();
        visits.forEach(recordList::add);
    }

    private void updateMainLabel(PatientDTO patient){
        mainLabel.setText(createMainLabelText(patient));
    }

    private String createMainLabelText(PatientDTO patient){
        LocalDate birthday = null;
        if( patient.birthday != null && !"0000-00-00".equals(patient.birthday) ){
            birthday = LocalDate.parse(patient.birthday);
        }
        Sex sex = Sex.fromCode(patient.sex);
        return String.format("(%d) %s %s (%s %s) %s生 %s才 %s性",
                patient.patientId,
                patient.lastName, patient.firstName,
                patient.lastNameYomi, patient.firstNameYomi,
                birthday != null ? DateTimeUtil.toKanji(birthday, DateTimeUtil.kanjiFormatter1) : "??",
                birthday != null ? DateTimeUtil.calcAge(birthday) + "": "??",
                sex != null ? sex.getKanji() : "??"
                );
    }


}
