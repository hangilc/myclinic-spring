package jp.chang.myclinic.reception.javafx;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;

public class WqueueTable extends TableView<WqueueFullDTO> {

    public WqueueTable(){
        getStyleClass().add("wqueue-table");
        TableColumn<WqueueFullDTO, String> waitStateColumn = new TableColumn<>("状態");
        waitStateColumn.setCellValueFactory(feature -> {
            String label = WqueueWaitState.codeToLabel(feature.getValue().wqueue.waitState);
            return new SimpleStringProperty(label);
        });
        waitStateColumn.getStyleClass().add("state-column");
        waitStateColumn.setCellFactory(param -> new TableCell<WqueueFullDTO, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                if( !empty ){
                    WqueueFullDTO wqueue = (WqueueFullDTO)getTableRow().getItem();
                    if( wqueue != null ) {
                        WqueueWaitState state = WqueueWaitState.fromCode(wqueue.wqueue.waitState);
                        if (state != null) {
                            TableRow tableRow = getTableRow();
                            tableRow.getStyleClass().removeAll("wait-cashier", "wait-drug");
                            switch (state) {
                                case WaitCashier:
                                    tableRow.getStyleClass().add("wait-cashier");
                                    break;
                                case WaitDrug:
                                    tableRow.getStyleClass().add("wait-drug");
                                    break;
                            }
                        }
                    }
                }
            }
        });
        getColumns().add(waitStateColumn);

        TableColumn<WqueueFullDTO, Integer> patientIdColumn = new TableColumn<>("患者番号");
        patientIdColumn.setCellValueFactory(feature -> new SimpleIntegerProperty(feature.getValue().patient.patientId).asObject());
        patientIdColumn.setPrefWidth(62);
        patientIdColumn.setCellFactory(col -> new TableCell<WqueueFullDTO, Integer>(){
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : String.format("% 4d", item));
                setStyle("-fx-alignment: center");
            }
        });
        getColumns().add(patientIdColumn);

        TableColumn<WqueueFullDTO, String> nameColumn = new TableColumn<>("氏名");
        nameColumn.setCellValueFactory(feature -> {
            PatientDTO patient = feature.getValue().patient;
            String text = patient.lastName + " " + patient.firstName;
            return new SimpleStringProperty(text);
        });
        nameColumn.setPrefWidth(90);
        nameColumn.setCellFactory(col -> new TableCell<WqueueFullDTO, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
            }
        });
        getColumns().add(nameColumn);

        TableColumn<WqueueFullDTO, String> yomiColumn = new TableColumn<>("よみ");
        yomiColumn.setCellValueFactory(feature -> {
            PatientDTO patient = feature.getValue().patient;
            String text = patient.lastNameYomi + " " + patient.firstNameYomi;
            return new SimpleStringProperty(text);
        });
        yomiColumn.setPrefWidth(100);
        yomiColumn.setCellFactory(col -> new TableCell<WqueueFullDTO, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
            }
        });
        getColumns().add(yomiColumn);

        TableColumn<WqueueFullDTO, String> sexColumn = new TableColumn<>("性別");
        sexColumn.setCellValueFactory(feature -> {
            String text = Sex.codeToKanji(feature.getValue().patient.sex);
            return new SimpleStringProperty(text);
        });
        sexColumn.setPrefWidth(38);
        sexColumn.setCellFactory(col -> new TableCell<WqueueFullDTO, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle("-fx-alignment: center");
            }
        });
        getColumns().add(sexColumn);

        TableColumn<WqueueFullDTO, String> birthdayColumn = new TableColumn<>("生年月日");
        birthdayColumn.setCellValueFactory(feature -> {
            PatientDTO patient = feature.getValue().patient;
            String text;
            try {
                text = DateTimeUtil.sqlDateToKanji(patient.birthday, DateTimeUtil.kanjiFormatter2);
            } catch(Exception ex){
                text = "";
            }
            return new SimpleStringProperty(text);
        });
        birthdayColumn.setPrefWidth(112);
        birthdayColumn.setCellFactory(col -> new TableCell<WqueueFullDTO, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle("-fx-alignment: center");
            }
        });
        getColumns().add(birthdayColumn);

        TableColumn<WqueueFullDTO, String> ageColumn = new TableColumn<>("年齢");
        ageColumn.setCellValueFactory(feature -> {
            PatientDTO patient = feature.getValue().patient;
            String text;
            try {
                LocalDate d = LocalDate.parse(patient.birthday);
                text = String.format("% 2d才", DateTimeUtil.calcAge(d));
            } catch(Exception ex){
                text = "";
            }
            return new SimpleStringProperty(text);
        });
        ageColumn.setPrefWidth(40);
        ageColumn.setCellFactory(col -> new TableCell<WqueueFullDTO, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle("-fx-alignment: center");
            }
        });
        getColumns().add(ageColumn);
    }

    public void printColumnWidths(){
        getColumns().forEach(col -> {
            System.out.printf("%s %f\n", col.getText(), col.getWidth());
        });
    }

}
