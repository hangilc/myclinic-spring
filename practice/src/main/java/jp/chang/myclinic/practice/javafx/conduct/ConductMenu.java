package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductDrugDTO;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.practice.javafx.events.ConductEnteredEvent;
import jp.chang.myclinic.practice.lib.PracticeAPI;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConductMenu extends VBox {

    private static Logger logger = LoggerFactory.getLogger(ConductMenu.class);

    private int visitId;
    private String at;
    private StackPane workarea = new StackPane();

    public ConductMenu(int visitId, String at) {
        super(4);
        this.visitId = visitId;
        this.at = at;
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
                MenuItem item = new MenuItem("注射追加");
                item.setOnAction(evt -> doEnterInjection());
                contextMenu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("全部コピー");
                item.setOnAction(evt -> doCopyAll());
                contextMenu.getItems().add(item);
            }
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
        }
    }

    private void fireConductEntered(ConductFullDTO entered){
        fireEvent(new ConductEnteredEvent(entered));
    }

    private void doEnterXp(){
        if( PracticeUtil.confirmCurrentVisitAction(visitId, "Ｘ線検査を入力しますか？") ) {
            EnterXpForm form = new EnterXpForm() {
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
    }

    private void doEnterInjection(){
        if( PracticeUtil.confirmCurrentVisitAction(visitId, "処置注射を入力しますか？") ){
            EnterInjectionForm form = new EnterInjectionForm(at){
                @Override
                protected void onEnter(EnterInjectionForm form, ConductKind kind, ConductDrugDTO drug) {
                    PracticeAPI.enterInjection(visitId, kind, drug)
                            .thenAccept(entered -> Platform.runLater(() -> {
                                fireConductEntered(entered);
                                hideWorkarea();
                            }))
                            .exceptionally(HandlerFX::exceptionally);
                }

                @Override
                protected void onCancel(EnterInjectionForm form) {
                    hideWorkarea();
                }
            };
            showWorkarea(form);
        }
    }

    private void doCopyAll(){
        int targetVisitId = PracticeUtil.findCopyTarget(visitId);
        if( targetVisitId > 0 ){
            Service.api.copyAllConducts(targetVisitId, visitId)
                    .thenCompose(Service.api::listConductFullByIds)
                    .thenAccept(entered -> Platform.runLater(() -> {
                        entered.forEach(this::fireConductEntered);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
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
