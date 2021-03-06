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
    private boolean success;

    ScannerDialog(String deviceId, Path savePath, int resolution) {
        setTitle("スキャン実行中");
        task = new ScanTask(deviceId, savePath, resolution,
                percent -> Platform.runLater(() -> progressBar.setProgress(percent / 100.0)));
        this.setOnCloseRequest(evt -> doCancel());
        Parent mainPanel = createMainPanel();
        setScene(new Scene(mainPanel));
        this.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                start(savePath);
            }
        });
    }

    private void start(Path savePath) {
        CompletableFuture.runAsync(task)
                .whenCompleteAsync((result, ex) -> {
                    if (!task.isCancelled() && ex == null) {
                        ScannerDialog.this.success = true;
                    } else {
                        try {
                            Files.deleteIfExists(savePath);
                        } catch (IOException e) {
                            logger.error("Failed to delete file. {}", e);
                        }
                        if (ex != null) {
                            Platform.runLater(() -> GuiUtil.alertError("エラー：" + ex));
                        } else {
                            String errorMessage = task.getErrorMessage();
                            if (errorMessage != null && !errorMessage.isEmpty()) {
                                Platform.runLater(() -> GuiUtil.alertError("エラー：" + errorMessage));
                            }
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

    public boolean isSuccess() {
        return success;
    }

    private void doCancel() {
        task.setCancelled(true);
    }

}
