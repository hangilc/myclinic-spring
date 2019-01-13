package jp.chang.myclinic.practice.javafx.disease.edit;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.practice.lib.Result;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DiseaseUtil;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.dateinput.DateForm;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;
import jp.chang.myclinic.utilfx.dateinput.DateFormLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Form extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Form.class);
    private Text nameText;
    private DateForm startDateForm;
    private DateForm endDateForm;
    private ChoiceBox<DiseaseEndReason> reasonChoice;
    private ByoumeiMasterDTO byoumeiMaster;
    private List<ShuushokugoMasterDTO> adjList;
    private LocalDate endDateSave;

    public Form(DiseaseFullDTO disease) {
        super(4);
        nameText = new Text("");
        startDateForm = new DateForm(Gengou.Recent, Gengou.Current);
        endDateForm = new DateForm(Gengou.Recent, Gengou.Current);
        getChildren().addAll(
                new TextFlow(new Label("名称："), nameText),
                startDateForm,
                new Label("から"),
                endDateForm,
                createReasonChoice()
        );
        setDisease(disease);
        reasonChoice.valueProperty().addListener((obs, oldValue, newValue) -> onReasonChange(newValue));
    }

    private Node createReasonChoice(){
        reasonChoice = new ChoiceBox<>();
        reasonChoice.setConverter(new StringConverter<>(){
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

    public int getShoubyoumeicode(){
        return byoumeiMaster.shoubyoumeicode;
    }

    public List<ShuushokugoMasterDTO> getShuushokugoMasters(){
        return adjList;
    }

    public Result<LocalDate, List<String>> getStartDate(){
        ErrorMessages em = new ErrorMessages();
        DateFormInputs inputs = startDateForm.getDateFormInputs();
        LocalDate date = DateFormLogic.dateFormInputsToLocalDate(inputs, "", em);
        return em.hasError() ? Result.createError(em.getMessages()) : Result.createValue(date);
    }

    public Result<LocalDate, List<String>> getEndDate(){
        ErrorMessages em = new ErrorMessages();
        DateFormInputs inputs = endDateForm.getDateFormInputs();
        LocalDate date = DateFormLogic.dateFormInputsToNullableLocalDate(inputs, "", em);
        return em.hasError() ? Result.createError(em.getMessages()) : Result.createValue(date);
    }

    public DiseaseEndReason getEndReason(){
        return reasonChoice.getValue();
    }

    private void setDisease(DiseaseFullDTO disease){
        byoumeiMaster = disease.master;
        adjList = new ArrayList<>();
        adjList.addAll(disease.adjList.stream().map(adj -> adj.master).collect(Collectors.toList()));
        startDateForm.setDateFormInputs(
                DateFormLogic.localDateToDateFormInputs(DateTimeUtil.parseSqlDate(disease.disease.startDate))
        );
        if( disease.disease.endDate == null || disease.disease.endDate.equals("0000-00-00") ){
            endDateForm.clear(Gengou.Current);
        } else {
            endDateForm.setDateFormInputs(
                    DateFormLogic.localDateToDateFormInputs(DateTimeUtil.parseSqlDate(disease.disease.endDate))
            );
        }
        reasonChoice.setValue(DiseaseEndReason.fromCode(disease.disease.endReason));
        updateName();
    }

    private void updateName(){
        nameText.setText(DiseaseUtil.getFullName(byoumeiMaster, adjList));
    }

    public void setByoumeiMaster(ByoumeiMasterDTO master){
        this.byoumeiMaster = master;
        updateName();
    }

    public void addShuushokugoMaster(ShuushokugoMasterDTO master){
        adjList.add(master);
        updateName();
    }

    public void deleteShuushokugoMaster(){
        adjList.clear();
        updateName();
    }

    private void onReasonChange(DiseaseEndReason newReason){
        if( newReason == DiseaseEndReason.NotEnded ){
            DateFormInputs inputs = endDateForm.getDateFormInputs();
            ErrorMessages em = new ErrorMessages();
            LocalDate currentDate = DateFormLogic.dateFormInputsToNullableLocalDate(inputs, "終了日", em);
            if( em.hasError() ){
                GuiUtil.alertError(em.getMessage());
            } else {
                endDateSave = currentDate;
                endDateForm.clear();
            }
        } else {
            if( endDateForm.isEmpty() && endDateSave != null ){
                endDateForm.setDateFormInputs(DateFormLogic.localDateToDateFormInputs(endDateSave));
            }
        }
    }

}
