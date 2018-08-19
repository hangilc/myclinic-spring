package jp.chang.myclinic.scanner;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

class ScannerDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(ScannerDialog.class);
    private ProgressBar progressBar;
    private ScanTask task;
    private Path savePath;
    private Path outPath;

    ScannerDialog(String deviceId, Path savePath) {
        this.savePath = savePath;
        setTitle("スキャン実行中");
        task = new ScanTask(deviceId, savePath, ScannerLib.getScannerResolutionSetting(),
                percent -> Platform.runLater(() -> progressBar.setProgress(percent / 100.0)));
        this.setOnCloseRequest(evt -> doCancel());
        Parent mainPanel = createMainPanel();
        setScene(new Scene(mainPanel));
    }

    public void start() {
        CompletableFuture.runAsync(task)
                .whenCompleteAsync((result, ex) -> {
                    if( !task.isCancelled() ){
                        try {
                            ScannerDialog.this.outPath = ScannerLib.convertImage(savePath, "jpg");
                            logger.info("saved image: {}", outPath);
                            Files.delete(savePath);
                        } catch(IOException e){
                            ex = e;
                        }
                    }
                    if( ex != null ){
                        GuiUtil.alertError("エラー：" + ex);
                    } else {
                        String errorMessage = task.getErrorMessage();
                        if( errorMessage != null && !errorMessage.isEmpty() ){
                            GuiUtil.alertError("エラー：" + errorMessage);
                        }
                    }
                    Platform.runLater(ScannerDialog.this::close);
                });
    }

    private Parent createMainPanel() {
        VBox vbox = new VBox(4);
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("scanner-dialog");
        vbox.getStylesheets().add("Scanner.css");
        this.progressBar = new ProgressBar(0);
        Button cancelButton = new Button("キャンセル");
        cancelButton.setOnAction(evt -> doCancel());
        vbox.getChildren().addAll(
                progressBar,
                cancelButton
        );
        return vbox;
    }

    public boolean isCancelled(){
        return task.isCancelled();
    }

    public Path getOutPath(){
        return outPath;
    }

    private void doCancel() {
        task.setCancelled(true);
    }

//    private void openAlertClosing() {
//        if (alertClosing != null) {
//            return;
//        }
//        Stage stage = new Stage();
//        stage.setTitle("アラート");
//        StackPane stackPane = new StackPane();
//        stackPane.getChildren().add(new Label("現在進行中のスキャンをキャンセルしています。"));
//        stackPane.setPadding(new Insets(10, 10, 10, 10));
//        stage.setScene(new Scene(stackPane));
//        stage.setOnCloseRequest(Event::consume);
//        this.alertClosing = stage;
//        stage.show();
//    }
}
