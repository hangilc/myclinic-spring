package jp.chang.myclinic.practice.javafx.conduct;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class EnterXpForm extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(EnterXpForm.class);

    private Supplier<String> gazouLabelSupplier;
    private Supplier<String> filmSupplier;

    public EnterXpForm() {
        super("X線入力");
        getChildren().addAll(
                createLabelInput(),
                createFilmInput(),
                createCommands()
        );
    }

    private Node createLabelInput(){
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("胸部単純Ｘ線", "腹部単純Ｘ線");
        combo.getSelectionModel().select(0);
        combo.setEditable(true);
        gazouLabelSupplier = () -> combo.getSelectionModel().getSelectedItem();
        return combo;
    }

    private Node createFilmInput(){
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("半切", "大角", "四ツ切");
        choiceBox.getSelectionModel().select("大角");
        filmSupplier = () -> choiceBox.getSelectionModel().getSelectedItem();
        return choiceBox;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> onEnter(this, gazouLabelSupplier.get(), filmSupplier.get()));
        cancelButton.setOnAction(evt -> onCancel(this));
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    protected void onEnter(EnterXpForm form, String label, String film){

    }

    protected void onCancel(EnterXpForm form){

    }

}
