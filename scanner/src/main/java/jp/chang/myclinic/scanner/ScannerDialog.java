package jp.chang.myclinic.scanner;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

class ScannerDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(ScannerDialog.class);
    private ProgressBar progressBar;
    private ScanTask task;
    private Stage alertClosing;
    private Path savePath;
    private Path outPath;

    ScannerDialog(String deviceId, Path savePath) {
        this.savePath = savePath;
        setTitle("スキャン実行中");
        task = new ScanTask(deviceId, savePath, ScannerLib.getScannerResolutionSetting(),
                percent -> Platform.runLater(() -> progressBar.setProgress(percent / 100.0)));
        this.setOnCloseRequest(evt -> {
            evt.consume();
            doCancel();
            openAlertClosing();
        });
        Parent mainPanel = createMainPanel();
        setScene(new Scene(mainPanel));
    }

    public CompletableFuture<Void> start() {
        return CompletableFuture.runAsync(task)
                .thenAccept(result -> {
                    if( !task.isCanceled() ){
                        try {
                            outPath = ScannerLib.convertImage(savePath, "jpg");
                            logger.info("saved image: {}", outPath);
                            Files.delete(savePath);
                        } catch(IOException ex){
                            throw new UncheckedIOException(ex);
                        }
                    }
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

    public boolean isCanceled() {
        return task.isCanceled();
    }

    public Path getOutPath() {
        return outPath;
    }

    private void doCancel() {
        task.setCanceled(true);
        openAlertClosing();
    }

    private void openAlertClosing() {
        if (alertClosing != null) {
            return;
        }
        Stage stage = new Stage();
        stage.setTitle("アラート");
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(new Label("現在進行中のスキャンをキャンセルしています。"));
        stackPane.setPadding(new Insets(10, 10, 10, 10));
        stage.setScene(new Scene(stackPane));
        stage.setOnCloseRequest(Event::consume);
        this.alertClosing = stage;
        stage.show();
    }
}
