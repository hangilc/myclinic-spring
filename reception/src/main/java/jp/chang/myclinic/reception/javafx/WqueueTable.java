package jp.chang.myclinic.reception.javafx;


import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.Sex;
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
        TableColumn<Wqueue, Number> waitStateColumn = new TableColumn<>("状態");
        waitStateColumn.setCellValueFactory(feature -> feature.getValue().waitStateProperty());
        waitStateColumn.getStyleClass().add("state-column");
        waitStateColumn.setCellFactory(param -> new TableCell<Wqueue, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    WqueueWaitState state = WqueueWaitState.fromCode(item.intValue());
                    if (state != null) {
                        setText(state.getLabel());
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
                } else {
                    setText("");
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
            return Bindings.concat(patient.lastNameProperty(), " ", patient.firstNameProperty());
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
            return Bindings.concat(patient.lastNameYomiProperty(), " ", patient.firstNameYomiProperty());
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

        TableColumn<Wqueue, Sex> sexColumn = new TableColumn<>("性別");
        sexColumn.setCellValueFactory(feature -> {
            return feature.getValue().getVisit().getPatient().sexProperty();
        });
        sexColumn.setPrefWidth(38);
        sexColumn.setCellFactory(col -> new TableCell<Wqueue, Sex>() {
            @Override
            protected void updateItem(Sex item, boolean empty) {
                super.updateItem(item, empty);
                if( !empty && item != null ){
                    setText(item.getKanji());
                } else {
                    setText("");
                }
                setStyle("-fx-alignment: center");
            }
        });
        getColumns().add(sexColumn);

        TableColumn<Wqueue, LocalDate> birthdayColumn = new TableColumn<>("生年月日");
        birthdayColumn.setCellValueFactory(feature -> {
            return feature.getValue().getVisit().getPatient().birthdayProperty();
        });
        birthdayColumn.setPrefWidth(112);
        birthdayColumn.setCellFactory(col -> new TableCell<Wqueue, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if( !empty && item != null ){
                    setText(DateTimeUtil.toKanji(item, DateTimeUtil.kanjiFormatter2));
                } else {
                    setText("");
                }
                setStyle("-fx-alignment: center");
            }
        });
        getColumns().add(birthdayColumn);

        TableColumn<Wqueue, LocalDate> ageColumn = new TableColumn<>("年齢");
        ageColumn.setCellValueFactory(feature -> {
            return feature.getValue().getVisit().getPatient().birthdayProperty();
        });
        ageColumn.setPrefWidth(40);
        ageColumn.setCellFactory(col -> new TableCell<Wqueue, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if( !empty && item != null ){
                    int age = DateTimeUtil.calcAge(item);
                    setText(age + "才");
                } else {
                    setText("");
                }
                setStyle("-fx-alignment: center");
            }
        });
        getColumns().add(ageColumn);
    }

    public CompletableFuture<WqueueFullDTO> getSelectedWqueueFullDTO() {
        Wqueue wqueue = getSelectionModel().getSelectedItem();
        if (wqueue == null) {
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
