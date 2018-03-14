package jp.chang.myclinic.practice.javafx.refer;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import jp.chang.myclinic.dto.ReferItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReferItemTable extends TableView<ReferItemDTO> {

    private static Logger logger = LoggerFactory.getLogger(ReferItemTable.class);

    public ReferItemTable() {
        TableColumn<ReferItemDTO, String> hospitalCol = new TableColumn<>("病院");
        hospitalCol.setCellValueFactory(feature -> new SimpleStringProperty(feature.getValue().hospital));
        getColumns().add(hospitalCol);

        TableColumn<ReferItemDTO, String> sectionCol = new TableColumn<>("診療科");
        sectionCol.setCellValueFactory(feature -> {
            String section = feature.getValue().section;
            if( section == null ){
                section = "";
            }
            return new SimpleStringProperty(section);
        });
        getColumns().add(sectionCol);

        TableColumn<ReferItemDTO, String> doctorCol = new TableColumn<>("医師");
        doctorCol.setCellValueFactory(feature -> {
            String doctor = feature.getValue().doctor;
            if( doctor == null ){
                doctor = "";
            }
            return new SimpleStringProperty(doctor);
        });
        getColumns().add(doctorCol);
    }

}
