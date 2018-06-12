package jp.chang.myclinic.recordbrowser;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.utilfx.Nav;

import java.time.LocalDate;
import java.util.List;

class PatientHistoryRoot extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(PatientHistoryRoot.class);

    private RecordListByPatient recordList = new RecordListByPatient();
    private Nav nav = new Nav();
    private Stage detailDialog;
    private Stage byoumeiDialog;

    PatientHistoryRoot(PatientDTO patient) {
        super(2);
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        ScrollPane recordScroll = new ScrollPane(recordList);
        recordScroll.getStyleClass().add("record-scroll");
        recordScroll.setFitToWidth(true);
        getChildren().addAll(
                createMainLabel(patient),
                createNavRow(patient),
                recordScroll
        );
    }

    void setupOnClose(Stage stage){
        stage.showingProperty().addListener((obs, oldValue, newValue) -> {
            if( oldValue && !newValue ){
                if( detailDialog != null ){
                    detailDialog.close();
                }
                if( byoumeiDialog != null ){
                    byoumeiDialog.close();
                }
            }
        });
    }

    void trigger() {
        nav.trigger();
    }

    private Node createNavRow(PatientDTO patient) {
        HBox hbox = new HBox(4);
        ByPatientNavHandler navHandler = new ByPatientNavHandler(patient);
        navHandler.setPageCallback(this::pageCallback);
        nav.setHandler(navHandler);
        Button refreshButton = new Button("更新");
        refreshButton.setOnAction(evt -> nav.trigger());
        hbox.getChildren().addAll(
                nav,
                refreshButton
        );
        return hbox;
    }

    private void pageCallback(List<VisitFull2DTO> visits) {
        recordList.clear();
        visits.forEach(recordList::add);
    }

    private Node createMainLabel(PatientDTO patient) {
        TextFlow mainLabel = new TextFlow();
        Hyperlink detailLink = new Hyperlink("追加情報");
        Hyperlink byoumeiLink = new Hyperlink("病名");
        detailLink.setOnAction(evt -> {
            if( detailDialog == null ) {
                detailDialog = new PatientDetailDialog(patient);
                detailDialog.setOnCloseRequest(e -> {
                    detailDialog = null;
                });
                detailDialog.show();
            } else {
                detailDialog.toFront();
            }
        });
        byoumeiLink.setOnAction(evt -> {
            Service.api.listCurrentDiseaseFull(patient.patientId)
                    .thenAccept(diseases -> Platform.runLater(() -> {
                        if( byoumeiDialog == null ) {
                            byoumeiDialog = new ByoumeiDialog(patient, diseases,
                                    ByoumeiDialog.SearchMode.Current);
                            byoumeiDialog.setOnCloseRequest(e -> byoumeiDialog = null);
                            byoumeiDialog.show();
                        } else {
                            byoumeiDialog.toFront();
                        }
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        });
        mainLabel.getChildren().addAll(
                new Text(makeMainLabelText(patient)),
                new Text(" "),
                detailLink,
                new Text(" "),
                byoumeiLink
        );
        return mainLabel;
    }

    private String makeMainLabelText(PatientDTO patient) {
        LocalDate birthday = null;
        if (patient.birthday != null && !"0000-00-00".equals(patient.birthday)) {
            birthday = LocalDate.parse(patient.birthday);
        }
        Sex sex = Sex.fromCode(patient.sex);
        return String.format("(%d) %s %s (%s %s) %s生 %s才 %s性",
                patient.patientId,
                patient.lastName, patient.firstName,
                patient.lastNameYomi, patient.firstNameYomi,
                birthday != null ? DateTimeUtil.toKanji(birthday, DateTimeUtil.kanjiFormatter1) : "??",
                birthday != null ? DateTimeUtil.calcAge(birthday) + "" : "??",
                sex != null ? sex.getKanji() : "??"
        );
    }


}
