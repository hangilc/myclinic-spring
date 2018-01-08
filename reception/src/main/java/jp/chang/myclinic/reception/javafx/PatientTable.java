package jp.chang.myclinic.reception.javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.lib.DateUtil;

import java.util.List;

public class PatientTable extends TableView<PatientTable.Model> {

    public static class Model {
        private IntegerProperty patientId = new SimpleIntegerProperty();
        private StringProperty name = new SimpleStringProperty();
        private StringProperty yomi = new SimpleStringProperty();
        private StringProperty birthday = new SimpleStringProperty();
        private StringProperty sex = new SimpleStringProperty();
        public PatientDTO orig;

        public static Model fromPatient(PatientDTO src){
            Model model = new Model();
            model.patientId.set(src.patientId);
            model.name.setValue(src.lastName + " " + src.firstName);
            model.yomi.setValue(src.lastNameYomi + " " + src.firstNameYomi);
            model.birthday.setValue(DateUtil.sqlDateToString(src.birthday));
            model.sex.setValue(Sex.codeToKanji(src.sex));
            model.orig = src;
            return model;
        }

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

        public String getYomi() {
            return yomi.get();
        }

        public StringProperty yomiProperty() {
            return yomi;
        }

        public void setYomi(String yomi) {
            this.yomi.set(yomi);
        }

        public String getBirthday() {
            return birthday.get();
        }

        public StringProperty birthdayProperty() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday.set(birthday);
        }

        public String getSex() {
            return sex.get();
        }

        public StringProperty sexProperty() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex.set(sex);
        }
    }

    public PatientTable(){
        setMaxWidth(Double.MAX_VALUE);

        TableColumn<PatientTable.Model, Integer> patientIdColumn = new TableColumn<>("患者番号");
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));

        TableColumn<PatientTable.Model, String> nameColumn = new TableColumn<>("名前");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<PatientTable.Model, String> yomiColumn = new TableColumn<>("よみ");
        yomiColumn.setCellValueFactory(new PropertyValueFactory<>("yomi"));
        yomiColumn.setComparator(String::compareTo);

        TableColumn<PatientTable.Model, String> birthdayColumn = new TableColumn<>("生年月日");
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));

        TableColumn<PatientTable.Model, String> sexColumn = new TableColumn<>("性別");
        sexColumn.setCellValueFactory(new PropertyValueFactory<>("sex"));

        getColumns().add(patientIdColumn);
        getColumns().add(nameColumn);
        getColumns().add(yomiColumn);
        getColumns().add(birthdayColumn);
        getColumns().add(sexColumn);
    }

    public void updateData(PatientDTO data){
        List<Model> models = getItems();
        boolean found = false;
        int index = 0;
        PatientTable.Model origModel = null;
        PatientTable.Model newModel = null;
        for(;index<models.size();index++){
            PatientTable.Model m = models.get(index);
            if( m.getPatientId() == data.patientId ){
                origModel = m;
                newModel = PatientTable.Model.fromPatient(data);
                found = true;
                break;
            }
        }
        if( found ){
            PatientTable.Model currentSelection = getSelectionModel().getSelectedItem();
            if( currentSelection == origModel ){
                currentSelection = newModel;
            }
            getItems().set(index, newModel);
            getSelectionModel().select(currentSelection);
            sort();
        }
    }

}
