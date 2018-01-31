package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DiseaseUtil;

public class Disease extends TextFlow {

    public Disease(DiseaseFullDTO disease){
        getChildren().addAll(
                makeNameLabel(disease),
                new Text(" "),
                makeStartAtLabel(disease)
        );
    }

    private Node makeNameLabel(DiseaseFullDTO disease){
        return new Text(makeNameText(disease));
    }

    private String makeNameText(DiseaseFullDTO diseaseFull){
        return DiseaseUtil.getFullName(diseaseFull);
    }

    private Node makeStartAtLabel(DiseaseFullDTO disease){
        String labelText = "(" + makeStartAtText(disease) + ")";
        Text text = new Text(labelText);
        text.getStyleClass().add("current-disease-start-at");
        return text;
    }

    private String makeStartAtText(DiseaseFullDTO diseaseFull){
        return DateTimeUtil.sqlDateToKanji(diseaseFull.disease.startDate, DateTimeUtil.kanjiFormatter5);
    }

}
