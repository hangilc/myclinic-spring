package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
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
        Hyperlink auxMenu = new Hyperlink("[+]");
        mainMenu.setOnAction(event -> {
            DrugForm form = new DrugEnterForm(patientId, visitId, at);
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
