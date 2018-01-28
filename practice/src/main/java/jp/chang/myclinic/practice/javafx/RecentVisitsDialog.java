package jp.chang.myclinic.practice.javafx;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.VisitPatientDTO;

import java.util.List;

public class RecentVisitsDialog extends Stage {

    public RecentVisitsDialog(List<VisitPatientDTO> list){
        StackPane root = new StackPane();
        VisitPatientTable table = new VisitPatientTable();
        table.getItems().setAll(list);
        setScene(new Scene(root));
    }
}
