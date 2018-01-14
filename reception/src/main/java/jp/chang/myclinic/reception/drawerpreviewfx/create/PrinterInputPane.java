package jp.chang.myclinic.reception.drawerpreviewfx.create;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

class PrinterInputPane extends VBox {

    PrinterInputPane(){
        super(4);
        Label dispLabel = new Label();
        dispLabel.setWrapText(true);
        dispLabel.setPrefHeight(Control.USE_COMPUTED_SIZE);
        dispLabel.setText("(未選択)");
        Button choosePrinterButton = new Button("プリンター選択");
        getChildren().addAll(dispLabel, choosePrinterButton);
    }

}
