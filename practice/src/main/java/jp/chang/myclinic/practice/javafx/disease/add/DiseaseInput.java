package jp.chang.myclinic.practice.javafx.disease.add;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.practice.lib.Result;
import jp.chang.myclinic.util.DiseaseUtil;
import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.utilfx.dateinput.DateForm;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;
import jp.chang.myclinic.utilfx.dateinput.DateFormLogic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiseaseInput extends VBox {

    private Text nameText = new Text("");
    private DateForm dateForm = new DateForm(Gengou.Recent, Gengou.Current);
    private ByoumeiMasterDTO byoumeiMaster;
    private List<ShuushokugoMasterDTO> adjList = new ArrayList<>();

    public DiseaseInput() {
        super(4);
        dateForm.setDateFormInputs(DateFormLogic.localDateToDateFormInputs(LocalDate.now()));
        getChildren().addAll(
                createName(),
                dateForm
        );
    }

    public void setByoumei(ByoumeiMasterDTO byoumeiMaster){
        this.byoumeiMaster = byoumeiMaster;
        updateName();
    }

    public ByoumeiMasterDTO getByoumei(){
        return byoumeiMaster;
    }

    public List<ShuushokugoMasterDTO> getShuushokugoList(){
        return adjList;
    }

    public void addShuushokugo(ShuushokugoMasterDTO shuushokugoMaster){
        this.adjList.add(shuushokugoMaster);
        updateName();
    }

    public void clearShuushokugo() {
        adjList.clear();
        updateName();
    }

    public void clear(){
        this.byoumeiMaster = null;
        adjList.clear();
        updateName();
    }

    public Result<LocalDate, List<String>> getStartDate(){
        DateFormInputs inputs = dateForm.getDateFormInputs();
        ErrorMessages em = new ErrorMessages();
        LocalDate date = DateFormLogic.dateFormInputsToLocalDate(inputs, "", em);
        if( em.hasError() ){
            return Result.createError(em.getMessages());
        } else {
            return Result.createValue(date);
        }
    }

    private Node createName() {
        return new TextFlow(new Label("名称："), nameText);
    }

    private void updateName(){
        String name = DiseaseUtil.getFullName(byoumeiMaster, adjList);
        nameText.setText(name);
    }
}
