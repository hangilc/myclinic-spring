package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.javafx.shinryou.ShinryouMenu;

import java.util.List;

class RecordShinryouPane extends VBox {

    private VBox shinryouList;

    RecordShinryouPane(List<ShinryouFullDTO> shinryouList, VisitDTO visit){
        getChildren().addAll(
                createMenu(visit.visitId),
                createShinryouList()
        );
        shinryouList.forEach(this::addShinryou);
    }

    private void addShinryou(ShinryouFullDTO shinryou){
        shinryouList.getChildren().add(new RecordShinryou(shinryou));
    }

    private Node createMenu(int visitId){
        return new ShinryouMenu(visitId);
    }

    private Node createShinryouList(){
        shinryouList = new VBox(2);
        return shinryouList;
    }
}
