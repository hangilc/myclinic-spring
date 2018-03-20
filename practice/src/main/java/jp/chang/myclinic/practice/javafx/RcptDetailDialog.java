package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.practice.javafx.parts.MeisaiDisp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RcptDetailDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(RcptDetailDialog.class);

    public RcptDetailDialog(MeisaiDTO meisai) {
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10px");
        root.getChildren().addAll(
                createDisp(meisai),
                createSum(meisai),
                createCommands()
        );
        setScene(new Scene(root));
    }

    private Node createDisp(MeisaiDTO meisai){
        return new MeisaiDisp(meisai);
    }

    private Node createSum(MeisaiDTO meisai){
        String text = String.format("総点 %,d 点", meisai.totalTen);
        return new Label(text);
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button closeButton = new Button("閉じる");
        closeButton.setOnAction(evt -> close());
        hbox.getChildren().addAll(closeButton);
        return hbox;
    }

}
