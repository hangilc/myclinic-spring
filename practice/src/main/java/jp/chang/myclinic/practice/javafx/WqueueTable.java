package jp.chang.myclinic.practice.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.WqueueFullDTO;

import java.util.List;

public class WqueueTable extends TableView<WqueueFullDTO> {

    public WqueueTable(List<WqueueFullDTO> list){
        getStyleClass().add("wqueue-table");

        TableColumn<WqueueFullDTO, String> waitStateColumn = new TableColumn<>("状態");
        waitStateColumn.setCellValueFactory(feature -> {
            String text = WqueueWaitState.codeToLabel(feature.getValue().wqueue.waitState);
            return new SimpleStringProperty(text);
        });
        waitStateColumn.setCellFactory(col -> new TableCell<WqueueFullDTO, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
            }
        });
        getColumns().add(waitStateColumn);

        TableColumn<WqueueFullDTO, String> patientNameColumn = new TableColumn<>("氏名");
        patientNameColumn.setCellValueFactory(feature -> {
            PatientDTO patient = feature.getValue().patient;
            String text = patient.lastName + " " + patient.firstName;
            return new SimpleStringProperty(text);
        });
        patientNameColumn.setCellFactory(col -> new TableCell<WqueueFullDTO, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
            }
        });
        getColumns().add(patientNameColumn);

        getItems().setAll(list);
    }

}
