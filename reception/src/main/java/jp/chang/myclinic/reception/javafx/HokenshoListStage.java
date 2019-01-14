package jp.chang.myclinic.reception.javafx;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.reception.Globals;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

class HokenshoListStage extends Stage {

    private static Logger logger = LoggerFactory.getLogger(HokenshoListStage.class);

    static class Model {
        String label;
        String file;

        Model(String label, String file) {
            this.label = label;
            this.file = file;
        }
    }

    HokenshoListStage(int patientId, List<Model> models) {
        setTitle("保険証画像リスト（" + patientId + "）");
        VBox root = new VBox(4);
        root.getStyleClass().addAll("dialog-root", "list-hokensho-dialog");
        root.getStylesheets().add("css/Main.css");
        if (models.size() == 0) {
            root.getChildren().add(new Label("（空白）"));
        } else {
            models.forEach(model -> {
                Hyperlink link = new Hyperlink(model.label);
                link.setOnAction(evt -> doOpenImage(patientId, model.file));
                root.getChildren().add(link);
            });
        }
        setScene(new Scene(root));
    }

    private void doOpenImage(int patientId, String file) {
        downloadImage(patientId, file, path -> {
            ImageDisplayStage imageStage = new ImageDisplayStage(path);
            imageStage.initOwner(this);
            imageStage.show();
        });
    }

    private void downloadImage(int patientId, String file, Consumer<Path> cb) {
        Path saveDir = Globals.getAppVars().getImageSaveDir();
        Path savePath = Paths.get(saveDir.toString(), file);
        if (!Files.exists(savePath)) {
            Service.api.getHokensho(patientId, file)
                    .thenAccept(responseBody -> {
                        try (FileOutputStream outStream = new FileOutputStream(savePath.toFile())) {
                            responseBody.byteStream().transferTo(outStream);
                            savePath.toFile().deleteOnExit();
                            Platform.runLater(() -> cb.accept(savePath));
                        } catch (IOException e) {
                            logger.error("Failed to download file. {}", file, e);
                            GuiUtil.alertError("Failed to download file.");
                        }
                    })
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            cb.accept(savePath);
        }
    }



}
