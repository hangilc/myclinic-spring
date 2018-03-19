package jp.chang.myclinic.practice.javafx.parts;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitPatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisitPatientTable extends TableView<VisitPatientDTO> {

    private static Logger logger = LoggerFactory.getLogger(VisitPatientTable.class);

    public VisitPatientTable() {
        getStyleClass().add("visit-patient-table");
        TableColumn<VisitPatientDTO, String> patientIdColumn = new TableColumn<>("患者番号");
        patientIdColumn.setCellValueFactory(feature -> {
            int patientId = feature.getValue().patient.patientId;
            String text = String.format("%04d", patientId);
            return new SimpleStringProperty(text);
        });
        patientIdColumn.setCellFactory(col -> new TableCell<VisitPatientDTO, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                getStyleClass().add("patient-id");
            }
        });
        getColumns().add(patientIdColumn);

        TableColumn<VisitPatientDTO, String> nameColumn = new TableColumn<>("名前");
        nameColumn.setCellValueFactory(feature -> {
            PatientDTO patient = feature.getValue().patient;
            String text = patient.lastName + " " + patient.firstName;
            return new SimpleStringProperty(text);
        });
        nameColumn.setCellFactory(col -> new TableCell<VisitPatientDTO, String>(){
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

