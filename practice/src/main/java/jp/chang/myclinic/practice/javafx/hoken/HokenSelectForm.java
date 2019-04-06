package jp.chang.myclinic.practice.javafx.hoken;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.VisitDTO;

public class HokenSelectForm extends VBox {

    public interface Callback {
        void onEnter(VisitDTO visit);
        void onCancel();
    }

    private Callback callback;
    private HokenSelectPane selectPane;

    public HokenSelectForm(HokenDTO available, HokenDTO current){
        super(4);
        getStyleClass().add("form");
        Label title = new Label("保険選択");
        title.getStyleClass().add("title");
        title.setMaxWidth(Double.MAX_VALUE);
        selectPane = new HokenSelectPane(available, current);
        getChildren().addAll(
                title,
                selectPane,
                createButtons()
        );
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private Node createButtons(){
        HBox hbox = new HBox(4);
        Hyperlink enterlink = new Hyperlink("入力");
        Hyperlink cancellink = new Hyperlink("キャンセル");
        enterlink.setOnAction(event -> {
            if( callback != null ){
                VisitDTO visit = new VisitDTO();
                selectPane.storeTo(visit);
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
