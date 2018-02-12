package jp.chang.myclinic.practice.javafx.parts;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TwoColumn extends HBox {

    private VBox leftBox = new VBox(0);
    private VBox rightBox = new VBox(0);

    public TwoColumn(int gap){
        super(0);
        int leftGap = gap / 2;
        int rightGap = gap - leftGap;
        leftBox.prefWidthProperty().bind(widthProperty().divide(2));
        rightBox.prefWidthProperty().bind(widthProperty().divide(2));
        leftBox.setOpaqueInsets(new Insets(0, leftGap, 0, 0));
        rightBox.setOpaqueInsets(new Insets(0, 0, 0, rightGap));
        getChildren().addAll(leftBox, rightBox);
    }

    public VBox getLeftBox() {
        return leftBox;
    }

    public VBox getRightBox() {
        return rightBox;
    }
}
