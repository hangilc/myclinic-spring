package jp.chang.myclinic.practice.javafx.disease;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.Gengou;

public class Add extends VBox {

    private DiseaseInput diseaseInput = new DiseaseInput();
    private DiseaseSearchBox searchBox;

    public Add(){
        super(4);
        diseaseInput.setGengou(Gengou.Current);
        searchBox = DiseaseSearchBox.create(() -> diseaseInput.getStartDate());
        searchBox.setOnSelectCallback(m -> m.applyTo(diseaseInput));
        getChildren().addAll(
                diseaseInput,
                searchBox
        );
    }
}
