package jp.chang.myclinic.practice.javafx.conduct;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GazouLabelDisp extends HBox {

    private static Logger logger = LoggerFactory.getLogger(GazouLabelDisp.class);

    public GazouLabelDisp(String label) {
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        Hyperlink editLink = new Hyperlink("編集");
        editLink.setOnAction(evt -> onEdit());
        getChildren().addAll(new Label("画像ラベル："), new Label(label), editLink);
    }

    protected void onEdit(){

    }

}
