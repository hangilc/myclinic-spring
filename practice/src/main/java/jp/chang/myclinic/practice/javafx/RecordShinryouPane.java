package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.javafx.shinryou.AddRegularForm;
import jp.chang.myclinic.practice.javafx.shinryou.ShinryouMenu;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class RecordShinryouPane extends VBox {

    private VBox shinryouList;
    private ShinryouMenu menu;
    private Consumer<List<ConductFullDTO>> onConductsEnteredHandler = cc -> {};

    RecordShinryouPane(List<ShinryouFullDTO> shinryouList, VisitDTO visit,
                       Map<Integer, ShinryouAttrDTO> shinryouAttrMap){
        getChildren().addAll(
                createMenu(visit),
                createShinryouList()
        );
        shinryouList.forEach(s -> addShinryou(s, shinryouAttrMap.get(s.shinryou.shinryouId)));
    }

    public void setOnConductsEnteredHandler(Consumer<List<ConductFullDTO>> onConductsEnteredHandler) {
        this.onConductsEnteredHandler = onConductsEnteredHandler;
    }

    void simulateEnterRegularShinryouClick() {
        menu.simulateEnterRegularChoice();
    }

    private RecordShinryou createRecordShinryou(ShinryouFullDTO shinryou, ShinryouAttrDTO attr){
        RecordShinryou recordShinryou = new RecordShinryou(shinryou, attr);
        recordShinryou.setOnDeletedHandler(() -> deleteShinryou(shinryou.shinryou.shinryouId));
        return recordShinryou;
    }

    private void addShinryou(ShinryouFullDTO shinryou, ShinryouAttrDTO attr){
        shinryouList.getChildren().add(createRecordShinryou(shinryou, attr));
    }

    void insertShinryou(ShinryouFullDTO shinryou, ShinryouAttrDTO attr){
        int i = 0;
        int shinryoucode = shinryou.shinryou.shinryoucode;
        RecordShinryou newRecordShinryou = createRecordShinryou(shinryou, attr);
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
        this.menu = new ShinryouMenu(visit);
        menu.setOnShinryouEnteredHandler((shinryouList, attrMap) -> {
            for(ShinryouFullDTO shinryou: shinryouList){
                insertShinryou(shinryou, attrMap.get(shinryou.shinryou.shinryouId));
            }
        });
        menu.setOnConductsEnteredHandler(cc -> onConductsEnteredHandler.accept(cc));
        menu.setOnDeletedHandler(deletedShinryouIds -> deletedShinryouIds.forEach(this::deleteShinryou));
        return menu;
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

    void deleteShinryou(int shinryouId) {
        RecordShinryou recordShinryou = findRecordShinryou(shinryouId);
        if( recordShinryou != null ){
            shinryouList.getChildren().remove(recordShinryou);
        }
    }

    Optional<AddRegularForm> findAddRegularForm() {
        return menu.findAddRegularForm();
    }

    List<RecordShinryou> listShinryou() {
        return shinryouList.getChildren().stream()
                .filter(n -> n instanceof RecordShinryou)
                .map(n -> (RecordShinryou)n)
                .collect(Collectors.toList());
    }

}
