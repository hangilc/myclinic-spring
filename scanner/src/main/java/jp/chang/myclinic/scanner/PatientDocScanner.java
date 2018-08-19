package jp.chang.myclinic.scanner;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PatientDocScanner extends Stage {

    private static Logger logger = LoggerFactory.getLogger(PatientDocScanner.class);
    private int patientId;
    private boolean scanningHokensho;
    private String timeStamp;
    private Path saveDir;
    private static DateTimeFormatter timeStampFormatter = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss");
    private IntegerProperty numberOfScannedPages = new SimpleIntegerProperty(0);
    private IntegerProperty currentPreviewPage = new SimpleIntegerProperty(0);
    private String deviceId;
    private List<Path> savedFilePaths = new ArrayList<>();
    private StackPane previewImageWrapper;

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
        this.previewImageWrapper = new StackPane();
        Label blankLabel = new Label("（空白）");
        previewImageWrapper.getChildren().add(blankLabel);
        currentPreviewPage.addListener((obs, oldValue, newValue) -> {
            int index = newValue.intValue() - 1;
            if (index >= 0 && index < savedFilePaths.size()) {
                Path path = savedFilePaths.get(index);
                setPreviewImage(path.toString());
            } else {
                previewImageWrapper.getChildren().setAll(blankLabel);
            }
        });
        previewImageWrapper.getStyleClass().add("preview-view");
        hbox.getChildren().addAll(
                previewImageWrapper,
                createPreviewControlPane()
        );
        return hbox;
    }

    private void setPreviewImage(String file) {
        Image image = new Image("file:" + file);
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(previewImageWrapper.widthProperty());
        imageView.fitHeightProperty().bind(previewImageWrapper.heightProperty());
        imageView.setPreserveRatio(true);
        previewImageWrapper.getChildren().setAll(imageView);
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
        prevButton.setDisable(true);
        nextButton.setDisable(true);
        currentPreviewPage.addListener((obs, oldValue, newValue) -> {
            prevButton.setDisable(newValue.intValue() <= 1);
        });
        BooleanBinding disableNextButton = Bindings.createBooleanBinding(
                () -> currentPreviewPage.getValue() >= numberOfScannedPages.getValue(),
                currentPreviewPage,
                numberOfScannedPages
        );
        nextButton.disableProperty().bind(disableNextButton);
        BooleanBinding noCurrentPageBinding = Bindings.createBooleanBinding(
                () -> {
                    int i = currentPreviewPage.getValue() - 1;
                    int n = numberOfScannedPages.getValue();
                    return !(i >= 0 && i < n);
                },
                currentPreviewPage,
                numberOfScannedPages
        );
        rescanLink.disableProperty().bind(noCurrentPageBinding);
        deleteLink.disableProperty().bind(noCurrentPageBinding);
        prevButton.setOnAction(evt -> doPrev());
        nextButton.setOnAction(evt -> doNext());
        rescanLink.setOnAction(evt -> doRescan());
        deleteLink.setOnAction(evt -> doDelete());
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
        endButton.setOnAction(evt -> close());
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
        if (deviceId == null) {
            this.deviceId = resolveDeviceId();
            if (deviceId == null) {
                return;
            }
        }
        int outputIndex = numberOfScannedPages.getValue() + 1;
        String saveFileName = composeSaveFileName(outputIndex);
        Path savePath = saveDir.resolve(saveFileName);
        ScannerDialog scannerDialog = new ScannerDialog(deviceId, savePath, ScannerSetting.INSTANCE.dpi);
        scannerDialog.initOwner(this);
        scannerDialog.initModality(Modality.WINDOW_MODAL);
        scannerDialog.showAndWait();
        if (scannerDialog.isSuccess()) {
            String outputFileName = composeOutputFileName(outputIndex);
            Path outPath = saveDir.resolve(outputFileName);
            try {
                boolean ok = ScannerLib.convertImage(savePath, "jpg", outPath);
                Files.delete(savePath);
                if( ok ){
                    savedFilePaths.add(outPath);
                    int index = numberOfScannedPages.getValue() + 1;
                    numberOfScannedPages.setValue(index);
                    currentPreviewPage.setValue(index);
                } else {
                    GuiUtil.alertError("画像の変換に失敗しました。");
                }
            } catch(IOException ex){
                logger.error("Failed to convert image. {}", ex);
                GuiUtil.alertError("画像の変換に失敗しました。" + ex);
            }
        }
    }

    private void doRescan() {
        if (!GuiUtil.confirm("再スキャンを開始しますか？")) {
            return;
        }
        if (deviceId == null) {
            this.deviceId = resolveDeviceId();
            if (deviceId == null) {
                return;
            }
        }
        int index = currentPreviewPage.getValue();
        if (!(index >= 1 && index <= numberOfScannedPages.getValue())) {
            logger.error("Invalid index");
            GuiUtil.alertError("Invalid page index");
            return;
        }
        String saveFileName = composeSaveFileName(index);
        Path savePath = saveDir.resolve(saveFileName);
        ScannerDialog scannerDialog = new ScannerDialog(deviceId, savePath, ScannerSetting.INSTANCE.dpi);
        scannerDialog.initOwner(this);
        scannerDialog.initModality(Modality.WINDOW_MODAL);
        scannerDialog.showAndWait();
        if (scannerDialog.isSuccess()) {
            Path outPath = savedFilePaths.get(index-1);
            try {
                boolean ok = ScannerLib.convertImage(savePath, "jpg", outPath);
                Files.delete(savePath);
                if( ok ){
                    setPreviewImage(outPath.toString());
                } else {
                    GuiUtil.alertError("画像の変換に失敗しました。");
                }
            } catch(IOException ex){
                logger.error("Failed to convert image. {}", ex);
                GuiUtil.alertError("画像の変換に失敗しました。" + ex);
            }
        }
    }

    private String composeSaveFileName(int index) {
        if (scanningHokensho) {
            return String.format("%d-hokensho-%s-%02d.bmp", patientId, timeStamp, index);
        } else {
            return String.format("%d-%s-%02d.bmp", patientId, timeStamp, index);
        }
    }

    private String composeOutputFileName(int index) {
        if (scanningHokensho) {
            return String.format("%d-hokensho-%s-%02d.jpg", patientId, timeStamp, index);
        } else {
            return String.format("%d-%s-%02d.jpg", patientId, timeStamp, index);
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

    private void doPrev() {
        int index = currentPreviewPage.getValue();
        if (index > 1) {
            index -= 1;
            currentPreviewPage.setValue(index);
        }
    }

    private void doNext() {
        int n = numberOfScannedPages.getValue();
        int i = currentPreviewPage.getValue();
        if (i < n) {
            i += 1;
            currentPreviewPage.setValue(i);
        }
    }

    private String changeSerial(String origFileName, int index){
        Pattern pat = Pattern.compile("(\\d+)\\.([^.]+)$");
        Matcher matcher = pat.matcher(origFileName);
        if( matcher.find() ){
            String s = String.format("%02d.$2", index);
            return matcher.replaceFirst(s);
        } else {
            return origFileName;
        }
    }

    private void reNumberSavedFiles() throws IOException {
        for(int i=0;i<savedFilePaths.size();i++){
            Path prevPath = savedFilePaths.get(i);
            String prevFile = prevPath.toString();
            String newFile = changeSerial(prevFile, i+1);
            Path newPath = Paths.get(newFile);
            if( !newFile.equals(prevFile) ){
                Files.move(prevPath, newPath, StandardCopyOption.REPLACE_EXISTING);
            }
            savedFilePaths.set(i, newPath);
        }
    }

    private void doDelete() {
        int i = currentPreviewPage.getValue();
        if (!(i >= 1 && i <= numberOfScannedPages.getValue())) {
            return;
        }
        if (!GuiUtil.confirm("このスキャン画像を削除していいですか？")) {
            return;
        }
        Path path = savedFilePaths.get(i - 1);
        try {
            Files.delete(path);
            savedFilePaths.remove(i - 1);
            reNumberSavedFiles();
            int n = numberOfScannedPages.getValue();
            n -= 1;
            if( n == 0 ){
                currentPreviewPage.setValue(0);
            } else {
                if( i > n ){
                    currentPreviewPage.setValue(n);
                } else {
                    setPreviewImage(savedFilePaths.get(i - 1).toString());
                }
            }
            numberOfScannedPages.setValue(n);
        } catch (IOException e) {
            logger.error("Failed to delete file. {}", e);
            GuiUtil.alertError("ファイルの削除に失敗しました。" + e);
        }
    }

}
