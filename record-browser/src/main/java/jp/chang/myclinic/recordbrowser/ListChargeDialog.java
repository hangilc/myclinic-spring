package jp.chang.myclinic.recordbrowser;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitChargePatientDTO;

import java.util.List;

class ListChargeDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(ListChargeDialog.class);

    ListChargeDialog(List<VisitChargePatientDTO> list) {
        setTitle("請求金額一覧");
        TableView<VisitChargePatientDTO> table = createTable();
        table.getItems().setAll(list);
        StackPane pane = new StackPane(table);
        pane.getStylesheets().add("Main.css");
        setScene(new Scene(pane));
    }

    private TableView<VisitChargePatientDTO> createTable(){
        TableView<VisitChargePatientDTO> table = new TableView<>();

        TableColumn<VisitChargePatientDTO, String> patientIdColumn = new TableColumn<>("患者番号");
        patientIdColumn.setCellValueFactory(feature -> {
            int patientId = feature.getValue().patient.patientId;
            String text = String.format("%04d", patientId);
            return new SimpleStringProperty(text);
        });
        patientIdColumn.getStyleClass().add("patient-id-column");
        table.getColumns().add(patientIdColumn);

        TableColumn<VisitChargePatientDTO, String> nameColumn = new TableColumn<>("患者氏名");
        nameColumn.setCellValueFactory(feature -> {
            PatientDTO patient = feature.getValue().patient;
            String name = String.format("%s %s", patient.lastName, patient.firstName);
            return new SimpleStringProperty(name);
        });
        nameColumn.getStyleClass().add("patient-name-column");
        table.getColumns().add(nameColumn);

        TableColumn<VisitChargePatientDTO, Number> chargeColumn = new TableColumn<>("請求金額");
        chargeColumn.setCellValueFactory(feature -> new SimpleIntegerProperty(feature.getValue().charge.charge));
        chargeColumn.setCellFactory(col -> new TableCell<>(){
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if( empty ){
                    setText("");
                } else {
                    String s = String.format("%,d円", item.intValue());
                    setText(s);
                }
                getStyleClass().add("charge-column");
            }
        });
        chargeColumn.getStyleClass().add("charge-column");
        table.getColumns().add(chargeColumn);

        return table;
    }

}
