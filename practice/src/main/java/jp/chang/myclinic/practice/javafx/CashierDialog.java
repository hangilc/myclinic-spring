package jp.chang.myclinic.practice.javafx;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.practice.javafx.parts.MeisaiDisp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CashierDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(CashierDialog.class);

    public CashierDialog(MeisaiDTO meisai) {
        VBox root = new VBox(4);
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        root.getStyleClass().add("cashier-dialog");
        root.getChildren().addAll(
                createDisp(meisai),
                createTotalTen(meisai.totalTen),
                createCharge(meisai)
        );
        setScene(new Scene(root));
    }

    private Node createDisp(MeisaiDTO meisai){
        MeisaiDisp disp = new MeisaiDisp(meisai);
        return disp;
    }

    private Node createTotalTen(int totalTen){
        HBox hbox = new HBox(4);
        hbox.getStyleClass().add("total-ten");
        String text = String.format("総点 %,d 点", totalTen);
        hbox.getChildren().add(new Label(text));
        hbox.setAlignment(Pos.BASELINE_RIGHT);
        return hbox;
    }

    private Node createCharge(MeisaiDTO meisai){
        StackPane wrapper = new StackPane();
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(String.format("請求額 %,d 円 (負担割 %d 割)", meisai.charge, meisai.futanWari));
        Hyperlink modifyLink = new Hyperlink("変更");
        modifyLink.setOnAction(evt -> {
            Node form = createEditForm(meisai);

            if( workarea.isVisible() ){
                workarea.hide();
            } else {
                workarea.show(form);
            }
        });
        hbox.getChildren().addAll(
                label,
                modifyLink
        );
        wrapper.getChildren().add(hbox);
        return wrapper;
    }

    private Node createChargeDisp(Region wrapper, int charge, int futanwari){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(String.format("請求額 %,d 円 (負担割 %d 割)", charge, futanwari));
        Hyperlink modifyLink = new Hyperlink("変更");
        modifyLink.setOnAction(evt -> {
            Node form = createEditForm(meisai);

            if( workarea.isVisible() ){
                workarea.hide();
            } else {
                workarea.show(form);
            }
        });
        hbox.getChildren().addAll(
                label,
                modifyLink
        );
        return hbox;
    }

    private Node createEditForm(){
        HBox hbox = new HBox(4);
        hbox.getChildren().addAll(
                new Label("変更額 ")
        );
        return hbox;
    }

}
