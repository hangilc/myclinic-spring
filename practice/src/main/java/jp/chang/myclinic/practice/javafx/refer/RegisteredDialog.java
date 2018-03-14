package jp.chang.myclinic.practice.javafx.refer;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.ReferItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RegisteredDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(RegisteredDialog.class);

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
        ReferItemTable table = new ReferItemTable();
        table.getItems().setAll(referList);
        return table;
    }

    private Node createCommands(){
        HBox root = new HBox(4);
        Button closeButton = new Button("閉じる");
        closeButton.setOnAction(evt -> close());
        root.getChildren().addAll(closeButton);
        return root;
    }

}
