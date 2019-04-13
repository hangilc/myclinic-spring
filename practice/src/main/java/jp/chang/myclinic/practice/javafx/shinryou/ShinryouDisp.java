package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import org.jetbrains.annotations.Nullable;

public class ShinryouDisp extends TextFlow {

    private Runnable onClickedHandler = () -> {};

    public ShinryouDisp(String masterName, ShinryouAttrDTO attr){
        getStyleClass().add("shinryou-disp");
        setContent(masterName, attr);
        setOnMouseClicked(evt -> onClickedHandler.run());
    }

    public void setOnClickedHandler(Runnable handler){
        this.onClickedHandler = handler;
    }

    private void setContent(String masterName, ShinryouAttrDTO attr){
        String label = masterName;
        String tekiyou = ShinryouAttrDTO.extractTekiyou(attr);
        if( tekiyou != null && !tekiyou.isEmpty() ){
                label += " [摘要：" + tekiyou + "]";
        }
        Text text = new Text(label);
        getChildren().setAll(text);
    }

}
