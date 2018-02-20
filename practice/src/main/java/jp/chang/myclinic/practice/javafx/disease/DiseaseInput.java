package jp.chang.myclinic.practice.javafx.disease;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.practice.javafx.parts.dateinput.DateInput;
import jp.chang.myclinic.practice.lib.Result;

import java.time.LocalDate;
import java.util.List;

public class DiseaseInput extends VBox {

    Text nameText = new Text("");
    DateInput dateInput = new DateInput();

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

    public Result<LocalDate, List<String>> getStartDate(){
        return dateInput.getValue();
    }

    private Node createName() {
        return new TextFlow(new Label("名称："), nameText);
    }
}
