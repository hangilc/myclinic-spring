package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import jp.chang.myclinic.practice.javafx.events.ConductEnteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConductMenu extends VBox {

    private static Logger logger = LoggerFactory.getLogger(ConductMenu.class);

    private int visitId;
    private StackPane workarea = new StackPane();

    public ConductMenu(int visitId) {
        super(4);
        this.visitId = visitId;
        getChildren().addAll(
            createLink()
        );
    }

    private Node createLink(){
        Hyperlink link = new Hyperlink("[処置]");
        link.setOnMouseClicked(this::onClick);
        return link;
    }

    private void onClick(MouseEvent event){
        if( !isWorkareaShown() ) {
            ContextMenu contextMenu = new ContextMenu();
            {
                MenuItem item = new MenuItem("Ｘ線検査追加");
                item.setOnAction(evt -> doEnterXp());
                contextMenu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("Ｘ線検査追加");
                contextMenu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("全部コピー");
                contextMenu.getItems().add(item);
            }
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
        }
    }

    private void fireConductEntered(ConductFullDTO entered){
        fireEvent(new ConductEnteredEvent(entered));
    }

    private void doEnterXp(){
        EnterXpForm form = new EnterXpForm(visitId){
            @Override
            protected void onEnter(EnterXpForm form, String label, String film) {
                Service.api.enterXp(visitId, label, film)
                        .thenCompose(Service.api::getConductFull)
                        .thenAccept(entered -> Platform.runLater(() -> {
                            fireConductEntered(entered);
                            hideWorkarea();
                        }))
                        .exceptionally(HandlerFX::exceptionally);
            }

            @Override
            protected void onCancel(EnterXpForm form) {
                hideWorkarea();
            }
        };
        showWorkarea(form);
    }

    private void showWorkarea(Node content){
        workarea.getChildren().clear();
        workarea.getChildren().add(content);
        getChildren().add(workarea);
    }

    private void hideWorkarea(){
        getChildren().remove(workarea);
    }

    private boolean isWorkareaShown(){
        return getChildren().contains(workarea);
    }

}
