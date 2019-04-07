package jp.chang.myclinic.practice.javafx.hoken;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.util.HokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HokenDisp extends TextFlow {

    private String dispText;
    private Runnable onClickHandler = () -> {};
    private final HokenDTO hoken;

    public HokenDisp(HokenDTO hoken){
        this.hoken = hoken;
        this.dispText = makeDispText(hoken);
        getStyleClass().add("hoken-disp");
        getChildren().add(new Text(dispText));
        setOnMouseClicked(evt -> onClickHandler.run());
    }

    public String getDispText(){
        return dispText;
    }

    public void setOnClickHandler(Runnable onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    public HokenDTO getHoken(){
        return hoken;
    }

    private String makeDispText(HokenDTO hoken) {
        String rep = HokenUtil.hokenRep(hoken);
        if (rep.isEmpty()) {
            rep = "(保険なし)";
        }
        return rep;
    }

}
