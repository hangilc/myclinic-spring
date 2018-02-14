package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class SearchEditForm<M,S> extends WorkForm {

    private static Logger logger = LoggerFactory.getLogger(SearchEditForm.class);

    private FormEditPart<M> editPart;

    public SearchEditForm(String title, FormEditPart<M> editPart, FormSearchPart<S> searchPart) {
        super(title);
        this.editPart = editPart;
        searchPart.selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            editPart.setModel(convertToModel(newValue));
        });
        getChildren().addAll(
                editPart.asNode(),
                createCommands(),
                searchPart.asNode()
        );
    }

    protected abstract M convertToModel(S searchResult);
    protected abstract M getBaseModel();

    private Node createCommands() {
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル ");
        enterButton.setOnAction(evt -> doEnter());
        cancelButton.setOnAction(evt -> onCancel());
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    private void doEnter(){
        M model = getBaseModel();
        List<String> errs = editPart.stuffInto(model);
        if( errs.size() > 0 ){
            GuiUtil.alertError(String.join("\n", errs));
            return;
        }
        onEnter(model);
    }

    protected void onEnter(M model){

    }

    protected void onCancel(){

    }

}
