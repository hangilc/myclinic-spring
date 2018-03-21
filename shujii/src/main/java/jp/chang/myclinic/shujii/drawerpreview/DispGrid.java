package jp.chang.myclinic.shujii.drawerpreview;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DispGrid extends GridPane {

    private static Logger logger = LoggerFactory.getLogger(DispGrid.class);

    private int row = 0;

    DispGrid() {
        setHgap(4);
        setVgap(4);
    }

    public int addRow(String label, Node content){
        addRow(row, new Label(label), content);
        return row++;
    }

    public int addRow(String label, Node content1, Node content2, Node... others){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(content1, content2);
        hbox.getChildren().addAll(others);
        return addRow(label, hbox);
    }

    public void rightAlignFirstColumn(){
        {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHalignment(HPos.RIGHT);
            getColumnConstraints().add(cc);
        }
        {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHalignment(HPos.LEFT);
            cc.setHgrow(Priority.ALWAYS);
            getColumnConstraints().add(cc);
        }
    }

    public void removeRow(int row){
        getChildren().removeIf(node -> {
            if( row == 0 ){
                return GridPane.getRowIndex(node) == null || GridPane.getRowIndex(node) == 0;
            } else {
                return GridPane.getRowIndex(node) == row;
            }
        });
    }

}
