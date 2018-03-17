package jp.chang.myclinic.practice.javafx.refer;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.ReferItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RegisteredDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(RegisteredDialog.class);
    private ReferItemTable table;

    public RegisteredDialog(List<ReferItemDTO> referList) {
        VBox root = new VBox(4);
        root.getStylesheets().add("css/Practice.css");
        root.getStyleClass().add("registered-refer-dialog");
        root.getChildren().addAll(
                createList(referList),
                createCommands()
        );
        setScene(new Scene(root));
    }

    private Node createList(List<ReferItemDTO> referList){
        table = new ReferItemTable();
        table.setOnMouseClicked(event -> {
            if( event.getButton().equals(MouseButton.PRIMARY) ){
                if( event.getClickCount() == 2 ){
                    doEnter();
                }
            }
        });
        table.getItems().setAll(referList);
        return table;
    }

    private Node createCommands(){
        HBox root = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> doEnter());
        cancelButton.setOnAction(evt -> close());
        root.getChildren().addAll(enterButton, cancelButton);
        return root;
    }

    private void doEnter(){
        ReferItemDTO item = table.getSelectionModel().getSelectedItem();
        if( item != null ){
            onEnter(this, item);
        }
    }

    void onEnter(RegisteredDialog self, ReferItemDTO item){

    }

}
