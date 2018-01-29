package jp.chang.myclinic.practice.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import jp.chang.myclinic.dto.PatientDTO;

public class PatientTable extends TableView<PatientDTO> {

    public PatientTable(){
        getStylesheets().add("css/PatientTable.css");

        TableColumn<PatientDTO, String> patientIdColumn = new TableColumn<>("患者番号");
        patientIdColumn.setCellValueFactory(feature -> {
            int patientId = feature.getValue().patientId;
            String text = String.format("%04d", patientId);
            return new SimpleStringProperty(text);
        });
        patientIdColumn.setCellFactory(col -> new TableCell<PatientDTO, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                getStyleClass().add("patient-id");
            }
        });
        getColumns().add(patientIdColumn);

        TableColumn<PatientDTO, String> nameColumn = new TableColumn<>("名前");
        nameColumn.setCellValueFactory(feature -> {
            PatientDTO patient = feature.getValue();
            String text = patient.lastName + " " + patient.firstName;
            return new SimpleStringProperty(text);
        });
        nameColumn.setCellFactory(col -> new TableCell<PatientDTO, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                getStyleClass().add("patient-name");
            }
        });
        getColumns().add(nameColumn);
    }

}
