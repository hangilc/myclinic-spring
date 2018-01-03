package jp.chang.myclinic.reception.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HokenTable extends TableView<HokenTable.Model> {

    public static class Model {
        private StringProperty name = new SimpleStringProperty("");
        private StringProperty validFrom = new SimpleStringProperty("");
        private StringProperty validUpto = new SimpleStringProperty("");
        private StringProperty honninKazoku = new SimpleStringProperty("");

        public static Model fromShahokokuho(ShahokokuhoDTO hoken){
            Model model = new Model();
            model.setName(ShahokokuhoUtil.rep(hoken));
            model.setValidFrom(validDateToString(hoken.validFrom));
            model.setValidUpto(validDateToString(hoken.validUpto));
            model.setHonninKazoku(hoken.honnin != 0 ? "本人" : "家族");
            return model;
        }

        public static Model fromKoukikourei(KoukikoureiDTO hoken){
            Model model = new Model();
            model.setName(KoukikoureiUtil.rep(hoken));
            model.setValidFrom(validDateToString(hoken.validFrom));
            model.setValidUpto(validDateToString(hoken.validUpto));
            return model;
        }

        public static Model fromRoujin(RoujinDTO hoken){
            Model model = new Model();
            model.setName(RoujinUtil.rep(hoken));
            model.setValidFrom(validDateToString(hoken.validFrom));
            model.setValidUpto(validDateToString(hoken.validUpto));
            return model;
        }

        public static Model fromKouhi(KouhiDTO hoken){
            Model model = new Model();
            model.setName(KouhiUtil.rep(hoken));
            model.setValidFrom(validDateToString(hoken.validFrom));
            model.setValidUpto(validDateToString(hoken.validUpto));
            return model;
        }

        private static String validDateToString(String sqlDate){
            if( sqlDate == null || sqlDate.equals("0000-00-00") ){
                return "";
            } else {
                return DateTimeUtil.sqlDateToKanji(sqlDate, DateTimeUtil.kanjiFormatter2);
            }
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

        public String getValidFrom() {
            return validFrom.get();
        }

        public StringProperty validFromProperty() {
            return validFrom;
        }

        public void setValidFrom(String validFrom) {
            this.validFrom.set(validFrom);
        }

        public String getValidUpto() {
            return validUpto.get();
        }

        public StringProperty validUptoProperty() {
            return validUpto;
        }

        public void setValidUpto(String validUpto) {
            this.validUpto.set(validUpto);
        }

        public String getHonninKazoku() {
            return honninKazoku.get();
        }

        public StringProperty honninKazokuProperty() {
            return honninKazoku;
        }

        public void setHonninKazoku(String honninKazoku) {
            this.honninKazoku.set(honninKazoku);
        }
    }

    public HokenTable(){
        setMaxWidth(Double.MAX_VALUE);

        TableColumn<Model, String> nameColumn = new TableColumn<>("種別");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Model, String> validFromColumn = new TableColumn<>("期限開始");
        validFromColumn.setCellValueFactory((new PropertyValueFactory<>("validFrom")));

        TableColumn<Model, String> validUptoColumn = new TableColumn<>("期限終了");
        validUptoColumn.setCellValueFactory((new PropertyValueFactory<>("validUpto")));

        TableColumn<Model, String> honninKazokuColumn = new TableColumn<>("本人・家族");
        honninKazokuColumn.setCellValueFactory((new PropertyValueFactory<>("honninKazoku")));

        getColumns().addAll(Arrays.asList(nameColumn, validFromColumn, validUptoColumn, honninKazokuColumn));
    }

    public void setHokenList(HokenListDTO hokenList){
        List<Model> models = new ArrayList<>();
        if( hokenList.shahokokuhoListDTO != null ){
            models.addAll(hokenList.shahokokuhoListDTO.stream().map(Model::fromShahokokuho).collect(Collectors.toList()));
        }
        if( hokenList.koukikoureiListDTO != null ){
            models.addAll(hokenList.koukikoureiListDTO.stream().map(Model::fromKoukikourei).collect(Collectors.toList()));
        }
        if( hokenList.roujinListDTO != null ){
            models.addAll(hokenList.roujinListDTO.stream().map(Model::fromRoujin).collect(Collectors.toList()));
        }
        if( hokenList.kouhiListDTO != null ){
            models.addAll(hokenList.kouhiListDTO.stream().map(Model::fromKouhi).collect(Collectors.toList()));
        }
        ObservableList<Model> items = FXCollections.observableList(models);
        setItems(items);
    }
}
