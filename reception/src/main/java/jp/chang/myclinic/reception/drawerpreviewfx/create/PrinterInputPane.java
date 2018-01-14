package jp.chang.myclinic.reception.drawerpreviewfx.create;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.drawer.printer.DevmodeInfo;
import jp.chang.myclinic.drawer.printer.DevnamesInfo;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;

class PrinterInputPane extends VBox {

    private Label dispLabel = new Label();
    private byte[] devnames;
    private byte[] devmode;

    PrinterInputPane(){
        super(4);
        dispLabel.setWrapText(true);
        dispLabel.setPrefWidth(260);
        dispLabel.setPrefHeight(40);
        dispLabel.setMaxHeight(Double.MAX_VALUE);
        dispLabel.setAlignment(Pos.CENTER_LEFT);
        dispLabel.setText("(未選択)");
        Button choosePrinterButton = new Button("プリンター選択");
        choosePrinterButton.setOnAction(event -> doChoosePrinter());
        VBox.setVgrow(dispLabel, Priority.ALWAYS);
        getChildren().addAll(dispLabel, choosePrinterButton);
    }

    public byte[] getDevmode() {
        return devmode;
    }

    public void setDevmode(byte[] devmode) {
        this.devmode = devmode;
    }

    public byte[] getDevnames() {
        return devnames;
    }

    public void setData(byte[] devmodeData, byte[] devnamesData){
        this.devmode = devmodeData;
        this.devnames = devnamesData;
        updateLabel();
    }

    private void updateLabel(){
        if( devmode == null && devnames == null ){
            dispLabel.setText("(未選択)");
        } else if( devmode != null && devnames != null ) {
            DevnamesInfo devnamesInfo = new DevnamesInfo(devnames);
            DevmodeInfo devmodeInfo = new DevmodeInfo(devmode);
            String dispText = String.format("%s, %s, %s, %s, %s, %d copy",
                    devnamesInfo.getDevice(),
                    devmodeInfo.getDefaultSourceLabel(),
                    devmodeInfo.getPaperSizeLabel(),
                    devmodeInfo.getOrientationLabel(),
                    devmodeInfo.getPrintQualityLabel(),
                    devmodeInfo.getCopies()
            );
            dispLabel.setText(dispText);
        } else {
            dispLabel.setText("(inconsistent data)");
        }
    }

    private void doChoosePrinter(){
        DrawerPrinter drawerPrinter = new DrawerPrinter();
        DrawerPrinter.DialogResult dialogResult = drawerPrinter.printDialog(null, null);
        if( dialogResult.ok ){
            setData(dialogResult.devmodeData, dialogResult.devnamesData);
        }
    }
}
