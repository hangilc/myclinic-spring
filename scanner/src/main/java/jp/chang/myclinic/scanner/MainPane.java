package jp.chang.myclinic.scanner;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class MainPane extends BorderPane {

    private static Logger logger = LoggerFactory.getLogger(MainPane.class);

    MainPane() {
        setTop(createMenuBar());
        setCenter(createCenterPane());
    }

    private MenuBar createMenuBar(){
        MenuBar mbar = new MenuBar();
        {
            Menu menu = new Menu("ファイル");
            {
                MenuItem item = new MenuItem("終了");
                item.setOnAction(evt -> Platform.exit());
                menu.getItems().add(item);
            }
            mbar.getMenus().add(menu);
        }
        {
            Menu menu = new Menu("設定");
            {
                MenuItem item = new MenuItem("保存フォルダー");
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("DPI");
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("スキャナーデバイス");
                menu.getItems().add(item);
            }
            mbar.getMenus().add(menu);
        }
        return mbar;
    }

    private Node createCenterPane(){
        HBox hbox = new HBox(4);
        hbox.getStyleClass().add("main-pane");
        hbox.getStylesheets().add("Scanner.css");
        Button patientDocButton = new Button("患者書類");
        Button hokenshoButton = new Button("保険証");
        Button regularDocButton = new Button("一般書類");
        patientDocButton.setOnAction(evt -> doPatientDoc(false));
        hokenshoButton.setOnAction(evt -> doPatientDoc(true));
        regularDocButton.setOnAction(evt -> doRegularDoc());
        hbox.getChildren().addAll(patientDocButton, hokenshoButton, regularDocButton);
        return hbox;
    }

    private void doPatientDoc(boolean hokensho){
        GuiUtil.askForString("患者番号を入力してください。", "").ifPresent(input -> {
            try {
                int patientId = Integer.parseInt(input);
                PatientDocScanner patientDocScanner = new PatientDocScanner(patientId, hokensho);
                patientDocScanner.initOwner(getScene().getWindow());
                patientDocScanner.initModality(Modality.WINDOW_MODAL);
                patientDocScanner.showAndWait();
            } catch(NumberFormatException ex){
                GuiUtil.alertError("患者番号の入力が不適切です。");
            }
        });
    }

    private void doRegularDoc(){
        String deviceId = ScannerLib.getScannerDevice(GuiUtil::alertError);
        if( deviceId == null ){
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存先の指定");
        File file = fileChooser.showSaveDialog(getScene().getWindow());
        if( file == null ){
            return;
        }
        Path tmpPath = null;
        try {
            tmpPath = Files.createTempFile("scanner", ".bmp");
            ScannerDialog scannerDialog = new ScannerDialog(deviceId, tmpPath, ScannerSetting.INSTANCE.dpi);
            scannerDialog.initOwner(getScene().getWindow());
            scannerDialog.initModality(Modality.WINDOW_MODAL);
            scannerDialog.showAndWait();
            if( scannerDialog.isSuccess() ){
                boolean ok = ScannerLib.convertImage(tmpPath, "jpg", file.toPath());
                if( !ok ){
                    GuiUtil.alertError("画像の変換に失敗しました。");
                    Files.deleteIfExists(file.toPath());
                } else {
                    logger.info("Saved regular doc: " + file.toString());
                }
            }
        } catch(IOException ex){
            GuiUtil.alertError("一般書類のスキャンに失敗しました。" + ex);
        } finally {
            try {
                if (tmpPath != null) {
                    Files.deleteIfExists(tmpPath);
                }
            } catch (IOException ex){
                logger.error("Failed to delete tmp file." + ex);
            }
        }
    }

}
