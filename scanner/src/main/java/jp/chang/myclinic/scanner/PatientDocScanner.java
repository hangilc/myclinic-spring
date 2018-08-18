package jp.chang.myclinic.scanner;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jp.chang.myclinic.utilfx.GuiUtil;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class PatientDocScanner extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(PatientDocScanner.class);
    private int patientId;
    private boolean scanningHokensho;
    private String timeStamp;
    private Path saveDir;
    private static DateTimeFormatter timeStampFormatter = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss");
    private IntegerProperty numberOfScannedPages = new SimpleIntegerProperty(0);
    private IntegerProperty currentPreviewPage = new SimpleIntegerProperty(0);

    PatientDocScanner(int patientId, boolean scanningHokensho) {
        this.patientId = patientId;
        this.scanningHokensho = scanningHokensho;
        this.timeStamp = makeTimeStamp();
        this.saveDir = getSaveDir();
        if (scanningHokensho) {
            setTitle(String.format("保険証のスキャン（%d）", patientId));
        } else {
            setTitle(String.format("患者書類のスキャン（%d）", patientId));
        }
        setScene(new Scene(createMain()));
    }

    private Parent createMain() {
        VBox vbox = new VBox(4);
        vbox.getStyleClass().add("patient-doc-scanner-dialog");
        vbox.getStylesheets().add("Scanner.css");
        vbox.getChildren().addAll(
                createPatientInfoPane(),
                createCenterPane(),
                createCommandPane()
        );
        return vbox;
    }

    private Node createPatientInfoPane() {
        VBox vbox = new VBox(0);
        vbox.setAlignment(Pos.CENTER);
        Label scannedPagesLabel = new Label();
        scannedPagesLabel.textProperty().bind(Bindings.format("スキャンしたページ数：%d", numberOfScannedPages));
        vbox.getChildren().addAll(
                new Label(String.format("患者番号：%d", patientId)),
                scannedPagesLabel
        );
        return vbox;
    }

    private Node createCenterPane() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        StackPane imageWrapper = new StackPane();
        imageWrapper.getChildren().add(new Label("（空白）"));
        imageWrapper.getStyleClass().add("preview-view");
        hbox.getChildren().addAll(
                imageWrapper,
                createPreviewControlPane()
        );
        return hbox;
    }

    private Node createPreviewControlPane() {
        VBox vbox = new VBox(0);
        vbox.getStyleClass().add("preview-control");
        vbox.setAlignment(Pos.CENTER);
        Label pageIndexLabel = new Label();
        pageIndexLabel.textProperty().bind(Bindings.format("%d/%d", currentPreviewPage, numberOfScannedPages));
        Button prevButton = new Button("前へ");
        Button nextButton = new Button("次へ");
        Hyperlink rescanLink = new Hyperlink("再スキャン");
        Hyperlink deleteLink = new Hyperlink("削除");
        rescanLink.getStyleClass().add("rescan");
        deleteLink.getStyleClass().add("delete");
        vbox.getChildren().addAll(
                pageIndexLabel,
                new HBox(4, prevButton, nextButton),
                rescanLink,
                deleteLink
        );
        return vbox;
    }

    private Node createCommandPane() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER);
        Button startButton = new Button("スタート");
        Button endButton = new Button("終了");
        startButton.setOnAction(evt -> doStart());
        hbox.getChildren().addAll(
                startButton,
                endButton
        );
        return hbox;
    }

    private String makeTimeStamp() {
        LocalDateTime dt = LocalDateTime.now();
        return dt.format(timeStampFormatter);
    }

    private Path getSaveDir() {
        return ScannerSetting.INSTANCE.savingDir;
    }

    private void doStart() {
        String deviceId = resolveDeviceId();
        if (deviceId == null) {
            return;
        }
        String saveFileName = composeSaveFileName();
        Path savePath = saveDir.resolve(saveFileName);
        ScannerDialog scannerDialog = new ScannerDialog(deviceId, savePath);
        scannerDialog.initOwner(this);
        scannerDialog.initModality(Modality.WINDOW_MODAL);
        scannerDialog.start();
        scannerDialog.showAndWait();
    }

    private String composeSaveFileName() {
        int nextPageIndex = numberOfScannedPages.getValue() + 1;
        if (scanningHokensho) {
            return String.format("%d-hokensho-%s-%02d.bmp", patientId, timeStamp, nextPageIndex);
        } else {
            return String.format("%d-%s-%02d.bmp", patientId, timeStamp, nextPageIndex);
        }
    }

    private String resolveDeviceId() {
        String deviceId = ScannerLib.getSacnnerDeviceSetting();
        if (deviceId != null) {
            return deviceId;
        } else {
            return ScannerLib.chooseScannerDevice(GuiUtil::alertError);
        }
    }


}
