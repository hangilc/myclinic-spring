package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShinryouEditForm extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(ShinryouEditForm.class);

    public ShinryouEditForm(ShinryouFullDTO shinryou) {
        super("診療行為編集");
        getChildren().addAll(
                createDisp(shinryou),
                createCommands()
        );
    }

    private Node createDisp(ShinryouFullDTO shinryou){
        ShinryouInput input = new ShinryouInput();
        input.setMaster(shinryou.master);
        return input;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button deleteButton = new Button("削除");
        Button cancelButton = new Button("キャンセル");
        deleteButton.setOnAction(event -> onDelete(this));
        cancelButton.setOnAction(event -> onCancel(this));
        hbox.getChildren().addAll(deleteButton, cancelButton);
        return hbox;
    }

    protected void onDelete(ShinryouEditForm form){

    }

    protected void onCancel(ShinryouEditForm form){

    }

}
