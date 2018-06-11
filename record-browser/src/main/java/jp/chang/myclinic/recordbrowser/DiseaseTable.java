package jp.chang.myclinic.recordbrowser;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DiseaseUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class DiseaseTable extends TableView<DiseaseTable.Model> {

    //private static Logger logger = LoggerFactory.getLogger(DiseaseTable.class);

    public static class Model {
        public StringProperty state = new SimpleStringProperty();
        public StringProperty name = new SimpleStringProperty();
        public StringProperty startDate = new SimpleStringProperty();
        public StringProperty endDate = new SimpleStringProperty();

        public Model(DiseaseFullDTO disease){
            this.state.setValue(DiseaseEndReason.fromCode(disease.disease.endReason).getKanjiRep());
            this.name.setValue(DiseaseUtil.getFullName(disease));
            this.startDate.setValue(DateTimeUtil.sqlDateToKanji(disease.disease.startDate,
                    DateTimeUtil.kanjiFormatter2));
            this.endDate.setValue(convertToEndDate(disease.disease.endDate));
        }

        private String convertToEndDate(String sqldate){
            if( sqldate == null || "0000-00-00".equals(sqldate) ){
                return "(なし)";
            } else {
                return DateTimeUtil.sqlDateToKanji(sqldate, DateTimeUtil.kanjiFormatter2);
            }
        }

        public String getState() {
            return state.get();
        }

        public StringProperty stateProperty() {
            return state;
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public String getStartDate() {
            return startDate.get();
        }

        public StringProperty startDateProperty() {
            return startDate;
        }

        public String getEndDate() {
            return endDate.get();
        }

        public StringProperty endDateProperty() {
            return endDate;
        }
    }

    DiseaseTable() {
        getStyleClass().add("disease-table");
        setMaxWidth(Double.MAX_VALUE);

        TableColumn<Model, String> stateColumn = new TableColumn<>("転帰");
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        stateColumn.getStyleClass().add("state-column");

        TableColumn<Model, String> nameColumn = new TableColumn<>("病名");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.getStyleClass().add("name-column");

        TableColumn<Model, String> startDateColumn = new TableColumn<>("開始日");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startDateColumn.getStyleClass().add("start-date-column");

        TableColumn<Model, String> endDateColumn = new TableColumn<>("終了日");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endDateColumn.getStyleClass().add("end-date-column");

        getColumns().addAll(Arrays.asList(
                stateColumn,
                nameColumn,
                startDateColumn,
                endDateColumn
        ));
    }

    void setRows(List<DiseaseFullDTO> list){
        List<Model> models = list.stream().map(Model::new).collect(Collectors.toList());
        ObservableList<Model> items = FXCollections.observableList(models);
        setItems(items);
    }

}
