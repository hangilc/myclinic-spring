package jp.chang.myclinic.practice.javafx.disease.edit;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.practice.javafx.parts.dateinput.DateInput;
import jp.chang.myclinic.practice.lib.Result;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Form extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Form.class);
    private Text nameText;
    private DateInput startDateInput;
    private DateInput endDateInput;
    private ChoiceBox<DiseaseEndReason> reasonChoice;
    private ByoumeiMasterDTO byoumeiMaster;
    private List<ShuushokugoMasterDTO> adjList;

    public Form(DiseaseFullDTO disease) {
        super(4);
        nameText = new Text("");
        startDateInput = new DateInput();
        endDateInput = new DateInput();
        getChildren().addAll(
                new TextFlow(new Label("名称："), nameText),
                startDateInput,
                new Label("から"),
                endDateInput,
                createReasonChoice()
        );
        setDisease(disease);
    }

    private Node createReasonChoice(){
        reasonChoice = new ChoiceBox<DiseaseEndReason>();
        reasonChoice.setConverter(new StringConverter<DiseaseEndReason>(){
            @Override
            public String toString(DiseaseEndReason reason) {
                return reason.getKanjiRep();
            }

            @Override
            public DiseaseEndReason fromString(String string) {
                return DiseaseEndReason.fromKanjiRep(string);
            }
        });
        reasonChoice.getItems().addAll(DiseaseEndReason.values());
        return reasonChoice;
    }

    public Result<LocalDate, List<String>> getStartDate(){
        return startDateInput.getValue();
    }

    private void setDisease(DiseaseFullDTO disease){
        byoumeiMaster = disease.master;
        adjList = new ArrayList<>();
        adjList.addAll(disease.adjList.stream().map(adj -> adj.master).collect(Collectors.toList()));
        startDateInput.setValue(DateTimeUtil.parseSqlDate(disease.disease.startDate));
        if( disease.disease.endDate == null || disease.disease.endDate.equals("0000-00-00") ){
            endDateInput.clear();
            endDateInput.setGengou(Gengou.Current);
        } else {
            endDateInput.setValue(DateTimeUtil.parseSqlDate(disease.disease.endDate));
        }
        reasonChoice.setValue(DiseaseEndReason.fromCode(disease.disease.endReason));
    }

}
