package jp.chang.myclinic.practice.javafx;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.util.HokenUtil;

public class RecordHoken extends TextFlow {
    private String dispText;

    RecordHoken(HokenDTO hoken){
        this.dispText = makeDispText(hoken);
        getChildren().add(new Text(dispText));
    }

    public String getDispText(){
        return dispText;
    }

    private String makeDispText(HokenDTO hoken) {
        String rep = HokenUtil.hokenRep(hoken);
        if (rep.isEmpty()) {
            rep = "(保険なし)";
        }
        return rep;
    }

}
