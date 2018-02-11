package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.lib.PracticeUtil;

public class ShinryouMenu extends VBox {

    private int visitId;
    private StackPane workarea = new StackPane();

    public ShinryouMenu(int visitId){
        super(4);
        this.visitId = visitId;
        getChildren().addAll(
                createMenu()
        );
    }

    private Node createMenu(){
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(
                createMainMenu(),
                createAuxMenu()
        );
        return hbox;
    }

    private Node createMainMenu(){
        Hyperlink mainLink = new Hyperlink("[診療行為]");
        mainLink.setOnAction(event -> doMainMenu());
        return mainLink;
    }

    private Node createAuxMenu(){
        Hyperlink auxLink = new Hyperlink("[+]");
        auxLink.setOnMouseClicked(this::onAuxMenu);
        return auxLink;
    }

    private void doMainMenu(){
        if( isWorkareaEmpty() ) {
            if( PracticeUtil.confirmCurrentVisitAction(visitId, "診療行為を追加しますか？") ) {
                AddRegularForm form = new AddRegularForm(visitId) {
                    @Override
                    void onEntered(AddRegularForm form) {
                        hideWorkarea();
                    }

                    @Override
                    void onCancel() {
                        hideWorkarea();
                    }
                };
                showWorkarea(form);
            }
        }
    }

    private void onAuxMenu(MouseEvent event){
        if( isWorkareaEmpty() ){

        }
    }

    private boolean isWorkareaEmpty(){
        return workarea.getChildren().size() == 0;
    }

    private void showWorkarea(Node content){
        workarea.getChildren().clear();
        workarea.getChildren().add(content);
        getChildren().add(workarea);
    }

    private void hideWorkarea(){
        workarea.getChildren().clear();
        getChildren().remove(workarea);
    }
}
