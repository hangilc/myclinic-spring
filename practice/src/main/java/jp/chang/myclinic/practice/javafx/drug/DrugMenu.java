package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DrugMenu extends VBox {

    private int patientId;
    private StackPane workarea = new StackPane();

    public DrugMenu(int patientId){
        super(4);
        this.patientId = patientId;
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
        Hyperlink auxMenu = new Hyperlink("[+]");
        mainMenu.setOnAction(event -> {
            EnterDrugForm form = new EnterDrugForm(patientId);
            showWorkarea(form);
        });
        hbox.getChildren().addAll(mainMenu, auxMenu);
        return hbox;
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
