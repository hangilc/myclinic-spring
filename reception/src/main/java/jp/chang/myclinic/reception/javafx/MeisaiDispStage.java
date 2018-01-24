package jp.chang.myclinic.reception.javafx;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.MeisaiDTO;

public class MeisaiDispStage extends Stage {

    public MeisaiDispStage(MeisaiDTO meisai){
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10");
        root.getChildren().add(new MeisaiDetailPane(meisai));
        Button okButton = new Button("OK");
        okButton.setOnAction(event -> close());
        HBox buttons = new HBox(4);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.getChildren().add(okButton);
        root.getChildren().add(buttons);
        setScene(new Scene(root));
    }
}
