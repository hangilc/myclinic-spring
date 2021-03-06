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
import java.nio.file.Paths;

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
                item.setOnAction(evt -> doSetSavingDir());
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("DPI");
                item.setOnAction(evt -> doSetDpi());
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("スキャナーデバイス");
                item.setOnAction(evt -> doSetDefaultDevice());
                menu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("一般書類の保存先");
                item.setOnAction(evt -> doSetRegularDocSavingHint());
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
        fileChooser.setInitialDirectory(Globals.regularDocSavingDirHint.toFile());
        File file = fileChooser.showSaveDialog(getScene().getWindow());
        if( file == null ){
            return;
        }
        Path tmpPath = null;
        try {
            tmpPath = Files.createTempFile("scanner", ".bmp");
            ScannerDialog scannerDialog = new ScannerDialog(deviceId, tmpPath, Globals.dpi);
            scannerDialog.initOwner(getScene().getWindow());
            scannerDialog.initModality(Modality.WINDOW_MODAL);
            scannerDialog.showAndWait();
            if( scannerDialog.isSuccess() ){
                boolean ok = ScannerLib.convertImage(tmpPath, "jpg", file.toPath());
                if( !ok ){
                    GuiUtil.alertError("画像の変換に失敗しました。");
                    Files.deleteIfExists(file.toPath());
                } else {
                    Globals.regularDocSavingDirHint = file.toPath().getParent();
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

    private void doSetSavingDir(){
        SetSavingDirDialog dialog = new SetSavingDirDialog();
        dialog.initOwner(getScene().getWindow());
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.showAndWait();
        if( dialog.isEnterPushed() ){
            Path dir = Paths.get(dialog.getSavingDir());
            Globals.savingDir = dir;
            if( dialog.saveToSettingSelected() ){
                try {
                    ScannerSetting.INSTANCE.setSavingDir(dir);
                } catch(IOException ex){
                    logger.error("Failed to save saving dir. {}", ex);
                    GuiUtil.alertError("設定ファイルへの保存に失敗しました。");
                }
            }
        }
    }

    private void doSetDpi(){
        SetDpiDialog dialog = new SetDpiDialog();
        dialog.initOwner(getScene().getWindow());
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.showAndWait();
        if( dialog.isEnterPushed() ){
            int newDpi = dialog.getDpi();
            Globals.dpi = newDpi;
            if( dialog.saveToSettingSelected() ){
                try {
                    ScannerSetting.INSTANCE.setDpi(newDpi);
                } catch(IOException ex){
                    logger.error("Failed to save dpi. {}", ex);
                    GuiUtil.alertError("設定ファイルへの保存に失敗しました。");
                }
            }
        }
    }

    private void doSetDefaultDevice(){
        SetDefaultDeviceDialog dialog = new SetDefaultDeviceDialog();
        dialog.initOwner(getScene().getWindow());
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.showAndWait();
        if( dialog.isEnterPushed() ){
            String newValue = dialog.getDefaultDevice();
            Globals.defaultDevice = newValue;
            if( dialog.saveToSettingSelected() ){
                try {
                    ScannerSetting.INSTANCE.setDefaultDevice(newValue);
                } catch(IOException ex){
                    logger.error("Failed to save dpi. {}", ex);
                    GuiUtil.alertError("設定ファイルへの保存に失敗しました。");
                }
            }
        }
    }

    private void doSetRegularDocSavingHint(){
        SetRegularDocSavingHintDialog dialog = new SetRegularDocSavingHintDialog();
        dialog.initOwner(getScene().getWindow());
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.showAndWait();
        if( dialog.isEnterPushed() ){
            Path newValue = Paths.get(dialog.getSavingHint());
            Globals.regularDocSavingDirHint = newValue;
            if( dialog.saveToSettingSelected() ){
                try {
                    ScannerSetting.INSTANCE.setRegularDocSavingDirHint(newValue);
                } catch(IOException ex){
                    logger.error("Failed to save dpi. {}", ex);
                    GuiUtil.alertError("設定ファイルへの保存に失敗しました。");
                }
            }
        }
   }

}
