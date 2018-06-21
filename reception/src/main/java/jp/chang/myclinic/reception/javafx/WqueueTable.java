package jp.chang.myclinic.reception.javafx;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.reception.tracker.model.Patient;
import jp.chang.myclinic.reception.tracker.model.Wqueue;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public class WqueueTable extends TableView<Wqueue> {

    public WqueueTable() {
        getStyleClass().add("wqueue-table");
        TableColumn<Wqueue, String> waitStateColumn = new TableColumn<>("状態");
        waitStateColumn.setCellValueFactory(feature -> {
            String label = WqueueWaitState.codeToLabel(feature.getValue().getWaitState());
            return new SimpleStringProperty(label);
        });
        waitStateColumn.getStyleClass().add("state-column");
        waitStateColumn.setCellFactory(param -> new TableCell<Wqueue, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                if (!empty) {
                    Wqueue wqueue = (Wqueue) getTableRow().getItem();
                    if (wqueue != null) {
                        WqueueWaitState state = WqueueWaitState.fromCode(wqueue.getWaitState());
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

        TableColumn<Wqueue, Integer> patientIdColumn = new TableColumn<>("患者番号");
        patientIdColumn.setCellValueFactory(feature -> new SimpleIntegerProperty(feature.getValue()
                .getVisit().getPatient().getPatientId()).asObject());
        patientIdColumn.setPrefWidth(62);
        patientIdColumn.setCellFactory(col -> new TableCell<Wqueue, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : String.format("% 4d", item));
                setStyle("-fx-alignment: center");
            }
        });
        getColumns().add(patientIdColumn);

        TableColumn<Wqueue, String> nameColumn = new TableColumn<>("氏名");
        nameColumn.setCellValueFactory(feature -> {
            Patient patient = feature.getValue().getVisit().getPatient();
            String text = patient.getLastName() + " " + patient.getFirstName();
            return new SimpleStringProperty(text);
        });
        nameColumn.setPrefWidth(90);
        nameColumn.setCellFactory(col -> new TableCell<Wqueue, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
            }
        });
        getColumns().add(nameColumn);

        TableColumn<Wqueue, String> yomiColumn = new TableColumn<>("よみ");
        yomiColumn.setCellValueFactory(feature -> {
            Patient patient = feature.getValue().getVisit().getPatient();
            String text = patient.getLastNameYomi() + " " + patient.getFirstNameYomi();
            return new SimpleStringProperty(text);
        });
        yomiColumn.setPrefWidth(100);
        yomiColumn.setCellFactory(col -> new TableCell<Wqueue, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
            }
        });
        getColumns().add(yomiColumn);

        TableColumn<Wqueue, String> sexColumn = new TableColumn<>("性別");
        sexColumn.setCellValueFactory(feature -> {
            String text = feature.getValue().getVisit().getPatient().getSex().getKanji();
            return new SimpleStringProperty(text);
        });
        sexColumn.setPrefWidth(38);
        sexColumn.setCellFactory(col -> new TableCell<Wqueue, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle("-fx-alignment: center");
            }
        });
        getColumns().add(sexColumn);

        TableColumn<Wqueue, String> birthdayColumn = new TableColumn<>("生年月日");
        birthdayColumn.setCellValueFactory(feature -> {
            Patient patient = feature.getValue().getVisit().getPatient();
            String text;
            try {
                if( patient.getBirthday() != null ){
                    text = DateTimeUtil.toKanji(patient.getBirthday(),
                            DateTimeUtil.kanjiFormatter2);
                } else {
                    text = "";
                }
            } catch (Exception ex) {
                text = "";
            }
            return new SimpleStringProperty(text);
        });
        birthdayColumn.setPrefWidth(112);
        birthdayColumn.setCellFactory(col -> new TableCell<Wqueue, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle("-fx-alignment: center");
            }
        });
        getColumns().add(birthdayColumn);

        TableColumn<Wqueue, String> ageColumn = new TableColumn<>("年齢");
        ageColumn.setCellValueFactory(feature -> {
            Patient patient = feature.getValue().getVisit().getPatient();
            String text;
            try {
                LocalDate d = patient.getBirthday();
                if( d != null ) {
                    text = String.format("% 2d才", DateTimeUtil.calcAge(d));
                } else {
                    text = "";
                }
            } catch (Exception ex) {
                text = "";
            }
            return new SimpleStringProperty(text);
        });
        ageColumn.setPrefWidth(40);
        ageColumn.setCellFactory(col -> new TableCell<Wqueue, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle("-fx-alignment: center");
            }
        });
        getColumns().add(ageColumn);
    }

    public CompletableFuture<WqueueFullDTO> getSelectedWqueueFullDTO(){
        Wqueue wqueue = getSelectionModel().getSelectedItem();
        if( wqueue == null ){
            return CompletableFuture.completedFuture(null);
        } else {
            return Service.api.getWqueueFull(wqueue.getVisit().getVisitId());
        }
    }

    public void printColumnWidths() {
        getColumns().forEach(col -> {
            System.out.printf("%s %f\n", col.getText(), col.getWidth());
        });
    }

}
