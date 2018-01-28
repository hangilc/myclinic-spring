package jp.chang.myclinic.practice.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import jp.chang.myclinic.dto.VisitPatientDTO;

public class VisitPatientTable extends TableView<VisitPatientDTO> {

    public VisitPatientTable() {
        TableColumn<VisitPatientDTO, String> patientIdColumn = new TableColumn<>("patientId");
        patientIdColumn.setCellValueFactory(feature -> {
            int patientId = feature.getValue().patient.patientId;
            return new SimpleStringProperty(String.format("%04d", patientId));
        });
        getColumns().add(patientIdColumn);
    }
}
