package jp.chang.myclinic.practice.javafx.drug;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.javafx.events.DrugEnteredEvent;
import jp.chang.myclinic.practice.lib.DrugsCopier;
import jp.chang.myclinic.practice.lib.GuiUtil;
import jp.chang.myclinic.practice.lib.PracticeService;
import jp.chang.myclinic.practice.lib.PracticeUtil;

import java.util.List;

public class DrugMenu extends VBox {

    private int patientId;
    private int visitId;
    private String at;
    private StackPane workarea = new StackPane();

    public DrugMenu(int patientId, int visitId, String at){
        super(4);
        this.patientId = patientId;
        this.visitId = visitId;
        this.at = at;
        workarea.setVisible(false);
        workarea.setManaged(false);
        getChildren().addAll(
                createMenu(),
                workarea
        );
    }

    private Node createMenu(){
        HBox hbox = new HBox(4);
        Hyperlink mainMenu = new Hyperlink("[処方]");
        Hyperlink auxMenuLink = new Hyperlink("[+]");
        mainMenu.setOnAction(event -> {
            if( isWorkareaEmpty() ) {
                DrugForm form = new DrugEnterForm(patientId, visitId, at) {
                    @Override
                    protected void onClose(DrugForm form) {
                        hideWorkarea();
                    }
                };
                showWorkarea(form);
            }
        });
        auxMenuLink.setOnMouseClicked(event -> {
            if( isWorkareaEmpty() ) {
                ContextMenu contextMenu = createAuxMenu();
                contextMenu.show(auxMenuLink, event.getScreenX(), event.getScreenY());
            }
        });
        hbox.getChildren().addAll(mainMenu, auxMenuLink);
        return hbox;
    }

    private ContextMenu createAuxMenu(){
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(
            createCopyAllMenuItem(),
                createCopySelectedMenuItem(),
                createModifyDaysMenuItem(),
                createDeleteSelectedMenuItem()
        );
        return menu;
    }

    private MenuItem createCopyAllMenuItem(){
        MenuItem item = new MenuItem("全部コピー");
        item.setOnAction(event -> {
            int targetVisitId = PracticeUtil.findCopyTarget();
            if( targetVisitId == 0 || targetVisitId == visitId ){
                GuiUtil.alertError("コピー先を見つけられませんでした。");
                return;
            }
            PracticeService.listDrugFull(visitId)
                    .thenAccept(drugs -> {
                        new DrugsCopier(targetVisitId, drugs,
                                enteredDrug -> fireEvent(new DrugEnteredEvent(enteredDrug)),
                                () -> { }
                                );
                    });
        });
        return item;
    }

    private MenuItem createCopySelectedMenuItem(){
        MenuItem item = new MenuItem("部分コピー");
        item.setOnAction(evt -> {
            int targetVisitId = PracticeUtil.findCopyTarget();
            if( targetVisitId == 0 || targetVisitId == visitId ){
                GuiUtil.alertError("コピー先を見つけられませんでした。");
                return;
            }
            PracticeService.listDrugFull(visitId)
                    .thenAccept(drugs -> {
                        CopySelectedForm form = new CopySelectedForm(drugs){
                            @Override
                            protected void onEnter(List<DrugFullDTO> selected) {
                                new DrugsCopier(targetVisitId, selected,
                                        enteredDrug -> fireEvent(new DrugEnteredEvent(enteredDrug)),
                                        () -> hideWorkarea()
                                );
                            }

                            @Override
                            protected void onClose() {
                                hideWorkarea();
                            }
                        };
                        Platform.runLater(() -> showWorkarea(form));
                    });
        });
        return item;
    }

    private MenuItem createModifyDaysMenuItem(){
        MenuItem item = new MenuItem("日数変更");
        return item;
    }

    private MenuItem createDeleteSelectedMenuItem(){
        MenuItem item = new MenuItem("複数削除");
        return item;
    }

    private boolean isWorkareaEmpty(){
        return workarea.getChildren().size() == 0;
    }

    private void showWorkarea(Node content){
        workarea.getChildren().add(content);
        workarea.setManaged(true);
        workarea.setVisible(true);
    }

    private void hideWorkarea(){
        workarea.getChildren().clear();
        workarea.setVisible(false);
        workarea.setManaged(false);
    }

}
