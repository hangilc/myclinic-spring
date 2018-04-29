package jp.chang.myclinic.recordbrowser;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

class SelectDateDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(SelectDateDialog.class);
    private Optional<LocalDate> selectedValue = Optional.empty();

    SelectDateDialog() {
        setTitle("日付の選択");
        Pane root = createRoot();
        root.getStyleClass().add("select-date-dialog");
        root.getStylesheets().add("Main.css");
        setScene(new Scene(root));
        this.sizeToScene();
    }

    Optional<LocalDate> getValue(){
        return selectedValue;
    }

    private Pane createRoot(){
        VBox vbox = new VBox(4);
        DatePicker datePicker = new DatePicker();
        vbox.getChildren().add(datePicker);
        Button selectButton = new Button("選択");
        Button cancelButton = new Button("キャンセル");
        selectButton.setDisable(true);
        datePicker.valueProperty().addListener((obs, oldValue, newValue) -> {
            selectButton.setDisable(newValue == null);
        });
        selectButton.setOnAction(evt -> {
            LocalDate d = datePicker.getValue();
            if( d != null ){
                doSelect(d);
            }
        });
        cancelButton.setOnAction(evt -> doCancel());
        vbox.getChildren().add(new HBox(4, selectButton, cancelButton));
        return vbox;
    }

    private void doSelect(LocalDate localDate){
        selectedValue = Optional.of(localDate);
        close();
    }

    private void doCancel(){
        selectedValue = Optional.empty();
        close();
    }

}
