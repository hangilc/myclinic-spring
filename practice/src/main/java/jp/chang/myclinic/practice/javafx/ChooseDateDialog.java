package jp.chang.myclinic.practice.javafx;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.dateinput.DateForm;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

class ChooseDateDialog extends Stage {

    private DateForm dateForm;
    private Button enterButton;
    private Button cancelButton;
    private Runnable onCancelCallback = () -> {};
    private Consumer<LocalDate> onEnterCallback = d -> {};

    ChooseDateDialog(String title, String prompt) {
        setTitle(title);
        Parent root = createRoot(prompt);
        root.getStylesheets().add("css/Practice.css");
        root.getStyleClass().addAll("dialog");
        Scene scene = new Scene(root);
        setScene(scene);
    }

    public void setOnEnter(Consumer<LocalDate> callback){
        this.onEnterCallback = callback;
    }

    public void setDate(LocalDate date){
        DateFormInputs inputs = new DateFormInputs();
        inputs.setDate(date);
        this.dateForm.setDateFormInputs(inputs);
    }

    private Parent createRoot(String prompt){
        VBox vbox = new VBox(4);
        Label label = new Label(prompt);
        this.dateForm = new DateForm();
        this.enterButton = new Button("決定");
        this.cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> onEnter());
        cancelButton.setOnAction(evt -> onCancel());
        HBox commandBox = new HBox(4);
        commandBox.getChildren().addAll(enterButton, cancelButton);
        vbox.getChildren().addAll(label, dateForm, commandBox);
        return vbox;
    }

    private void onCancel(){
        close();
        this.onCancelCallback.run();
    }

    private void onEnter(){
        DateFormInputs inputs = dateForm.getDateFormInputs();
        Optional<LocalDate> optDate = inputs.getDate();
        if(optDate.isPresent()){
            LocalDate date = optDate.get();
            close();
            this.onEnterCallback.accept(date);
        } else {
            GuiUtil.alertError("日付が適切でありません。");
        }
    }

}
