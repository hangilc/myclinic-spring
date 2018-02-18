package jp.chang.myclinic.practice.javafx.conduct;

import javafx.scene.control.ComboBox;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.practice.javafx.parts.EnterCancelBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GazouLabelForm extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(GazouLabelForm.class);

    public GazouLabelForm(String value) {
        super("画像ラベルの編集");
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(PracticeUtil.gazouLabelExamples);
        combo.setEditable(true);
        combo.setValue(value);
        EnterCancelBox commands = new EnterCancelBox();
        commands.setEnterCallback(() -> onEnter(combo.getSelectionModel().getSelectedItem()));
        commands.setCancelCallback(this::onCancel);
        getChildren().addAll(
                combo,
                commands
        );
    }

    protected void onEnter(String value) {

    }

    protected void onCancel(){

    }

}
