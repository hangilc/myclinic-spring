package jp.chang.myclinic.practice.javafx;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.VisitPatientDTO;
import jp.chang.myclinic.practice.javafx.parts.VisitPatientTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TodaysVisitsDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(TodaysVisitsDialog.class);

    public TodaysVisitsDialog(List<VisitPatientDTO> list) {
        VBox root = new VBox(4);
        root.getStylesheets().add("css/Practice.css");
        {
            VisitPatientTable table = new VisitPatientTable();
            table.getItems().setAll(list);
            root.getChildren().add(table);
        }
        {
            HBox hbox = new HBox(4);
            Button selectButton = new Button("選択");
            Button closeButton = new Button("閉じる");
            hbox.getChildren().addAll(selectButton, closeButton);
            root.getChildren().add(hbox);
        }
        setScene(new Scene(root));
    }

}
