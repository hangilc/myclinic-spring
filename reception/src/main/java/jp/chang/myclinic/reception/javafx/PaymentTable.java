package jp.chang.myclinic.reception.javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jp.chang.myclinic.dto.PaymentVisitPatientDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentTable extends TableView<PaymentTable.Model> {

    public static class Model {
        private IntegerProperty patientId = new SimpleIntegerProperty();
        private StringProperty name = new SimpleStringProperty();
        private StringProperty amount = new SimpleStringProperty();
        private StringProperty at = new SimpleStringProperty();

        public int getPatientId() {
            return patientId.get();
        }

        public IntegerProperty patientIdProperty() {
            return patientId;
        }

        public void setPatientId(int patientId) {
            this.patientId.set(patientId);
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getAmount() {
            return amount.get();
        }

        public StringProperty amountProperty() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount.set(amount);
        }

        public String getAt() {
            return at.get();
        }

        public StringProperty atProperty() {
            return at;
        }

        public void setAt(String at) {
            this.at.set(at);
        }

        public static Model fromPayment(PaymentVisitPatientDTO payment){
            Model model = new Model();
            model.patientId.set(payment.patient.patientId);
            model.name.setValue(payment.patient.lastName + " " + payment.patient.firstName);
            model.amount.setValue(String.format("%,d", payment.payment.amount));
            model.at.setValue(DateTimeUtil.sqlDateTimeToKanji(payment.payment.paytime,
                    DateTimeUtil.kanjiFormatter2,
                    DateTimeUtil.kanjiFormatter4));
            return model;
        }
    }

    public PaymentTable(){
        TableColumn<Model, Integer> patientIdColumn = new TableColumn<>("患者番号");
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        patientIdColumn.setPrefWidth(65);

        TableColumn<Model, String> nameColumn = new TableColumn<>("氏名");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(80);

        TableColumn<Model, Integer> amountColumn = new TableColumn<>("金額");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setPrefWidth(52);
        amountColumn.setStyle("-fx-alignment: center-right");

        TableColumn<Model, String> atColumn = new TableColumn<>("日時");
        atColumn.setCellValueFactory(new PropertyValueFactory<>("at"));
        atColumn.setComparator(String::compareTo);
        atColumn.setPrefWidth(166);
        atColumn.setStyle("-fx-alignment: center");

        getColumns().add(patientIdColumn);
        getColumns().add(nameColumn);
        getColumns().add(amountColumn);
        getColumns().add(atColumn);
    }

    public void setRows(List<PaymentVisitPatientDTO> list){
        List<Model> models = list.stream().map(Model::fromPayment).collect(Collectors.toList());
        setItems(FXCollections.observableArrayList(models));
    }

    public void printColumnWidths(){
        for(TableColumn<Model,?> column: getColumns()){
            System.out.println(column.getId() + ":" + column.getWidth());
        }
    }
}
