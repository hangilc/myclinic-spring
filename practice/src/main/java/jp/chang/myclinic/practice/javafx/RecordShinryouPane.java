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
                createMenu(visit),
                createShinryouList()
        );
        shinryouList.forEach(this::addShinryou);
    }

    private void addShinryou(ShinryouFullDTO shinryou){
        shinryouList.getChildren().add(new RecordShinryou(shinryou));
    }

    public void insertShinryou(ShinryouFullDTO shinryou){
        int i = 0;
        int shinryoucode = shinryou.shinryou.shinryoucode;
        RecordShinryou newRecordShinryou = new RecordShinryou(shinryou);
        for(Node node: shinryouList.getChildren()){
            if( node instanceof RecordShinryou ){
                RecordShinryou r = (RecordShinryou)node;
                if( r.getShinryoucode() > shinryoucode ){
                    shinryouList.getChildren().add(i, newRecordShinryou);
                    return;
                }
            }
            i += 1;
        }
        shinryouList.getChildren().add(newRecordShinryou);
    }

    private Node createMenu(VisitDTO visit){
        return new ShinryouMenu(visit);
    }

    private Node createShinryouList(){
        shinryouList = new VBox(2);
        return shinryouList;
    }

    private RecordShinryou findRecordShinryou(int shinryouId){
        for(Node node: shinryouList.getChildren()){
            if( node instanceof RecordShinryou){
                RecordShinryou r = (RecordShinryou)node;
                if( r.getShinryouId() == shinryouId ){
                    return r;
                }
            }
        }
        return null;
    }

    public void deleteShinryou(int shinryouId) {
        RecordShinryou recordShinryou = findRecordShinryou(shinryouId);
        if( recordShinryou != null ){
            shinryouList.getChildren().remove(recordShinryou);
        }
    }
}
