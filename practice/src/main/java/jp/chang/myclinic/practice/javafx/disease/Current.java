package jp.chang.myclinic.practice.javafx.disease;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DiseaseFullDTO;

import java.util.List;

public class Current extends VBox {

    public Current(List<DiseaseFullDTO>list){
        super(4);
        list.forEach(this::addDisease);
    }

    private void addDisease(DiseaseFullDTO diseaseFull){
        Disp disp = new Disp(diseaseFull){
            @Override
            protected void onMouseClick(DiseaseFullDTO disease) {
                Current.this.onSelect(disease);
            }
        };
        getChildren().add(disp);
    }

    protected void onSelect(DiseaseFullDTO disease){

    }
}
