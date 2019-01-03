package jp.chang.myclinic.reception.javafx;


import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public class WqueueTable extends TableView<WqueueTable.Model> {

    public interface Model {
        IntegerProperty waitStateProperty();
        IntegerProperty patientIdProperty();
        StringProperty lastNameProperty();
        StringProperty firstNameProperty();
        StringProperty lastNameYomiProperty();
        StringProperty firstNameYomiProperty();
        ObjectProperty<Sex> sexProperty();
        ObjectProperty<LocalDate> birthdayProperty();
        int getVisitId();
    }

    public WqueueTable() {
        getStyleClass().add("wqueue-table");
        TableColumn<Model, Number> waitStateColumn = new TableColumn<>("状態");
        waitStateColumn.setCellValueFactory(feature -> feature.getValue().waitStateProperty());
        waitStateColumn.getStyleClass().add("state-column");
        waitStateColumn.setCellFactory(param -> new TableCell<>() {
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

        TableColumn<Model, Number> patientIdColumn = new TableColumn<>("患者番号");
        patientIdColumn.setCellValueFactory(feature -> feature.getValue().patientIdProperty());
        patientIdColumn.setPrefWidth(62);
        patientIdColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : String.format("% 4d", item.intValue()));
                setStyle("-fx-alignment: center");
            }
        });
        getColumns().add(patientIdColumn);

        TableColumn<Model, String> nameColumn = new TableColumn<>("氏名");
        nameColumn.setCellValueFactory(feature -> Bindings.concat(
                feature.getValue().lastNameProperty(), " ", feature.getValue().firstNameProperty()
        ));
        nameColumn.setPrefWidth(90);
        nameColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
            }
        });
        getColumns().add(nameColumn);

        TableColumn<Model, String> yomiColumn = new TableColumn<>("よみ");
       yomiColumn.setCellValueFactory(feature -> Bindings.concat(
                feature.getValue().lastNameYomiProperty(), " ", feature.getValue().firstNameYomiProperty()
        ));
        yomiColumn.setPrefWidth(100);
        yomiColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
            }
        });
        getColumns().add(yomiColumn);

        TableColumn<Model, Sex> sexColumn = new TableColumn<>("性別");
        sexColumn.setCellValueFactory(feature -> feature.getValue().sexProperty());
        sexColumn.setPrefWidth(38);
        sexColumn.setCellFactory(col -> new TableCell<>() {
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

        TableColumn<Model, LocalDate> birthdayColumn = new TableColumn<>("生年月日");
        birthdayColumn.setCellValueFactory(feature -> feature.getValue().birthdayProperty());
        birthdayColumn.setPrefWidth(112);
        birthdayColumn.setCellFactory(col -> new TableCell<>() {
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

        TableColumn<Model, LocalDate> ageColumn = new TableColumn<>("年齢");
        ageColumn.setCellValueFactory(feature -> feature.getValue().birthdayProperty());
        ageColumn.setPrefWidth(40);
        ageColumn.setCellFactory(col -> new TableCell<>() {
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

    public int getVisitIdOfSelectedWqueue(){
        Model wqueue = getSelectionModel().getSelectedItem();
        return wqueue == null ? 0 : wqueue.getVisitId();
    }

    public CompletableFuture<WqueueFullDTO> getSelectedWqueueFullDTO() {
        Model wqueue = getSelectionModel().getSelectedItem();
        if (wqueue == null) {
            return CompletableFuture.completedFuture(null);
        } else {
            return Service.api.getWqueueFull(wqueue.getVisitId());
        }
    }

    public void printColumnWidths() {
        getColumns().forEach(col -> {
            System.out.printf("%s %f\n", col.getText(), col.getWidth());
        });
    }

}
