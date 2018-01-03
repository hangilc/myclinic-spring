package jp.chang.myclinic.reception.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jp.chang.myclinic.dto.KouhiDTO;
import jp.chang.myclinic.dto.KoukikoureiDTO;
import jp.chang.myclinic.dto.RoujinDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.*;

import java.util.Arrays;

public class HokenTable extends TableView<HokenTable.Model> {

    public static class Model {
        private StringProperty name = new SimpleStringProperty("");
        private StringProperty validFrom = new SimpleStringProperty("");
        private StringProperty validUpto = new SimpleStringProperty("");
        private StringProperty honninKazoku = new SimpleStringProperty("");
        private StringProperty futanWari = new SimpleStringProperty("");

        public static Model fromShahokokuho(ShahokokuhoDTO hoken){
            Model model = new Model();
            model.setName(ShahokokuhoUtil.rep(hoken));
            model.setValidFrom(validDateToString(hoken.validFrom));
            model.setValidUpto(validDateToString(hoken.validUpto));
            model.setHonninKazoku(hoken.honnin != 0 ? "本人" : "家族");
            model.setFutanWari(hoken.kourei > 0 ? String.format("高齢%d割", hoken.kourei) : "");
            return model;
        }

        public static Model fromKoukikourei(KoukikoureiDTO hoken){
            Model model = new Model();
            model.setName(KoukikoureiUtil.rep(hoken));
            model.setValidFrom(validDateToString(hoken.validFrom));
            model.setValidUpto(validDateToString(hoken.validUpto));
            model.setFutanWari(String.format("%d割", hoken.futanWari));
            return model;
        }

        public static Model fromRoujin(RoujinDTO hoken){
            Model model = new Model();
            model.setName(RoujinUtil.rep(hoken));
            model.setValidFrom(validDateToString(hoken.validFrom));
            model.setValidUpto(validDateToString(hoken.validUpto));
            model.setFutanWari(String.format("%d割", hoken.futanWari));
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

        public String getFutanWari() {
            return futanWari.get();
        }

        public StringProperty futanWariProperty() {
            return futanWari;
        }

        public void setFutanWari(String futanWari) {
            this.futanWari.set(futanWari);
        }
    }

    public HokenTable(){
        TableColumn<Model, String> nameColumn = new TableColumn<>("種別");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Model, String> validFromColumn = new TableColumn<>("期限開始");
        validFromColumn.setCellValueFactory((new PropertyValueFactory<>("validFrom")));

        TableColumn<Model, String> validUptoColumn = new TableColumn<>("期限終了");
        validUptoColumn.setCellValueFactory((new PropertyValueFactory<>("validUpto")));

        TableColumn<Model, String> honninKazokuColumn = new TableColumn<>("本人・家族");
        honninKazokuColumn.setCellValueFactory((new PropertyValueFactory<>("honninKazoku")));

        TableColumn<Model, String> futanWariColumn = new TableColumn<>("負担割");
        futanWariColumn.setCellValueFactory((new PropertyValueFactory<>("futanWari")));

        getColumns().addAll(Arrays.asList(nameColumn, validFromColumn, validUptoColumn,
                honninKazokuColumn, futanWariColumn));
    }
}
