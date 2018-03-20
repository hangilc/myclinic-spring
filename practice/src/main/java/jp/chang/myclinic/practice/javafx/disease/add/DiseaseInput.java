package jp.chang.myclinic.practice.javafx.disease.add;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.practice.javafx.parts.dateinput.DateInput;
import jp.chang.myclinic.practice.lib.Result;
import jp.chang.myclinic.util.DiseaseUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiseaseInput extends VBox {

    private Text nameText = new Text("");
    private DateInput dateInput = new DateInput();
    private ByoumeiMasterDTO byoumeiMaster;
    private List<ShuushokugoMasterDTO> adjList = new ArrayList<>();

    public DiseaseInput() {
        super(4);
        dateInput.setValue(LocalDate.now());
        getChildren().addAll(
                createName(),
                dateInput
        );
    }

    public void setGengou(Gengou gengou){
        dateInput.setGengou(gengou);
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
        return dateInput.getValue();
    }

    private Node createName() {
        return new TextFlow(new Label("名称："), nameText);
    }

    private void updateName(){
        String name = DiseaseUtil.getFullName(byoumeiMaster, adjList);
        nameText.setText(name);
    }
}
