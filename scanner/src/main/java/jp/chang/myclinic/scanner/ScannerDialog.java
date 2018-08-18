package jp.chang.myclinic.scanner;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;

class ScannerDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(ScannerDialog.class);
    private boolean canceled = true;
    private Runnable task;

    ScannerDialog(String deviceId, Path savePath) {
        setTitle("スキャン実行中");
        task = new ScanTask(deviceId, savePath, ScannerLib.getScannerResolutionSetting(),
                percent -> System.out.println(percent),
                () -> System.out.println("success"),
                msg -> System.out.println(msg)
                );
        this.showingProperty().addListener((obs, oldValue, newValue) -> {
            if( newValue ){
                task.run();
            }
        });
        Parent mainPanel = createMainPanel();
        setScene(new Scene(mainPanel));
    }

    private Parent createMainPanel(){
        VBox vbox = new VBox(4);
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("scanner-dialog");
        vbox.getStylesheets().add("Scanner.css");
        ProgressBar progressBar = new ProgressBar(0);
        Button cancelButton = new Button("キャンセル");
        vbox.getChildren().addAll(
                progressBar,
                cancelButton
        );
        return vbox;
    }

    public boolean isCanceled(){
        return canceled;
    }

    private void setCanceled(boolean canceled){
        this.canceled = canceled;
    }


}
