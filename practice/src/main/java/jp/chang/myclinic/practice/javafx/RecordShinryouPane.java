package jp.chang.myclinic.practice.javafx;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ShinryouFullDTO;

import java.util.List;

public class RecordShinryouPane extends VBox {

    public RecordShinryouPane(List<ShinryouFullDTO> shinryouList){
        shinryouList.forEach(this::addShinryou);
    }

    private void addShinryou(ShinryouFullDTO shinryou){
        getChildren().add(new RecordShinryou(shinryou));
    }
}
