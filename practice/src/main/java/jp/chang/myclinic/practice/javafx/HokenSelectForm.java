package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.VisitDTO;

import java.util.Collections;
import java.util.List;

public class HokenSelectForm extends VBox {

    public interface Callback {
        void onEnter(VisitDTO visit);
        void onCancel();
    }

    private Callback callback;
    private HokenSelectPane selectPane;

    public HokenSelectForm(HokenDTO available, HokenDTO current){
        super(4);
        selectPane = new HokenSelectPane(available, current);
        getChildren().addAll(
                selectPane,
                createButtons()
        );
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private int getSelectedShahokokuhoId(){
        return 0;
    }

    private int getSelectedKoukikoureiId(){
        return 0;
    }

    private int getSelectedRoujinId(){
        return 0;
    }

    private List<Integer> getSelectedKouhiIds(){
        return Collections.emptyList();
    }

    private Node createButtons(){
        HBox hbox = new HBox(4);
        Hyperlink enterlink = new Hyperlink("入力");
        Hyperlink cancellink = new Hyperlink("キャンセル");
        enterlink.setOnAction(event -> {
            if( callback != null ){
                VisitDTO visit = new VisitDTO();
                visit.shahokokuhoId = getSelectedShahokokuhoId();
                visit.koukikoureiId = getSelectedKoukikoureiId();
                visit.roujinId = getSelectedRoujinId();
                List<Integer> kouhiIds = getSelectedKouhiIds();
                if( kouhiIds.size() > 0 ){
                    visit.kouhi1Id = kouhiIds.get(0);
                    if( kouhiIds.size() > 1 ){
                        visit.kouhi2Id = kouhiIds.get(1);
                        if( kouhiIds.size() > 2 ){
                            visit.kouhi3Id = kouhiIds.get(2);
                        }
                    }
                }
                callback.onEnter(visit);
            }
        });
        cancellink.setOnAction(event -> {
            if( callback != null ){
                callback.onCancel();
            }
        });
        hbox.getChildren().addAll(enterlink, cancellink);
        return hbox;
    }

}
