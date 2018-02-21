package jp.chang.myclinic.practice.javafx.parts;

import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispGrid extends GridPane {

    private static Logger logger = LoggerFactory.getLogger(DispGrid.class);

    private int row = 0;

    public DispGrid() {
        setHgap(4);
        setVgap(4);
    }

    public int addRow(String label, Node content){
        addRow(row, new Label(label), content);
        return row++;
    }

    public void rightAlignFirstColumn(){
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHalignment(HPos.RIGHT);
        getColumnConstraints().add(cc);
    }

}
