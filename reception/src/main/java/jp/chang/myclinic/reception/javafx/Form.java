package jp.chang.myclinic.reception.javafx;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class Form extends GridPane {

    private int currentRow = 0;

    public Form() {
        setHgap(4);
        setVgap(4);
        ColumnConstraints firstColumnConstraints = new ColumnConstraints();
        firstColumnConstraints.setPrefWidth(Control.USE_COMPUTED_SIZE);
        firstColumnConstraints.setMinWidth(Control.USE_PREF_SIZE);
        firstColumnConstraints.setMaxWidth(Control.USE_PREF_SIZE);
        firstColumnConstraints.setHalignment(HPos.RIGHT);
        getColumnConstraints().addAll(firstColumnConstraints);
    }

    public void add(String name, Node node){
        Label label = new Label(name);
        add(label, 0, currentRow);
        add(node, 1, currentRow);
        currentRow += 1;
    }

    public void addWithHbox(String name, Node... nodes){
        HBox hbox = makeHbox();
        hbox.getChildren().addAll(nodes);
        add(name, hbox);
    }

    public HBox makeHbox(){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }
}
