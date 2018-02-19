package jp.chang.myclinic.practice.javafx.disease;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.practice.javafx.parts.dateinput.DateInput;

public class DiseaseInput extends VBox {

    Text nameText = new Text("");
    DateInput dateInput = new DateInput();

    public DiseaseInput() {
        super(4);
        getChildren().addAll(
                dateInput,
                createName()
        );
    }

    public void setGengou(Gengou gengou){
        dateInput.setGengou(gengou);
    }

    private Node createName() {
        return new TextFlow(new Label("名称："), nameText);
    }
}
