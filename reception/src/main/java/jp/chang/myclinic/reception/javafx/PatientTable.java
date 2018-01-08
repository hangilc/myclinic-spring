package jp.chang.myclinic.reception.javafx;

import javafx.beans.binding.StringBinding;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jp.chang.myclinic.reception.model.PatientModel;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.Arrays;

public class PatientTable extends TableView<PatientModel> {

    /*
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
            String birthdayRep = "";
            {
                if( src.birthday != null && !src.birthday.equals("0000-00-00") ){
                    try {
                        birthdayRep = DateTimeUtil.sqlDateToKanji(src.birthday, DateTimeUtil.kanjiFormatter1);
                    } catch(Exception ex){
                        birthdayRep = "(Invalid)";
                    }
                }
            }
            model.birthday.setValue(birthdayRep);
            Sex sex = Sex.fromCode(src.sex);
            model.sex.setValue(sex == null ? "(不明)" : sex.getKanji());
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
    */

    public PatientTable(){
        setMaxWidth(Double.MAX_VALUE);

        TableColumn<PatientModel, Integer> patientIdColumn = new TableColumn<>("患者番号");
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));

        TableColumn<PatientModel, String> nameColumn = new TableColumn<>("名前");
        nameColumn.setCellValueFactory(value -> {
            PatientModel model = value.getValue();
            return model.lastNameProperty().concat(" ").concat(model.firstNameProperty());
        });

        TableColumn<PatientModel, String> yomiColumn = new TableColumn<>("よみ");
        yomiColumn.setCellValueFactory(value -> {
            PatientModel model = value.getValue();
            return model.lastNameYomiProperty().concat(" ").concat(model.firstNameYomiProperty());

        });

        TableColumn<PatientModel, String> birthdayColumn = new TableColumn<>("生年月日");
        birthdayColumn.setCellValueFactory(value -> new StringBinding(){

            { bind(value.getValue().birthdayProperty()); }

            @Override
            protected String computeValue() {
                LocalDate date = value.getValue().birthdayProperty().getValue();
                if( date == null || date == LocalDate.MAX ){
                    return "";
                } else {
                    return DateTimeUtil.toKanji(date, DateTimeUtil.kanjiFormatter1);
                }
            }
        });

        TableColumn<PatientModel, String> sexColumn = new TableColumn<>("性別");
        sexColumn.setCellValueFactory(value -> new StringBinding(){
            { bind(value.getValue().sexProperty()); }

            @Override
            protected String computeValue(){
                return value.getValue().getSex().getKanji();
            }
        });

        getColumns().addAll(Arrays.asList(patientIdColumn, nameColumn, yomiColumn, birthdayColumn, sexColumn));
    }

}
