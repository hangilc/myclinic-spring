package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
        return item;
    }

    private MenuItem createCopySelectedMenuItem(){
        MenuItem item = new MenuItem("部分コピー");
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
