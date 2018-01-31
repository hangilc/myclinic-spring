package jp.chang.myclinic.practice.javafx;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ChargeDTO;

class RecordCharge extends TextFlow {

    RecordCharge(ChargeDTO charge) {
        getChildren().add(new Text(getChargeText(charge)));
    }

    private String getChargeText(ChargeDTO charge){
        String label;
        if( charge != null ){
            label = String.format("請求額：%,d円", charge.charge);
        } else {
            label = "(未請求)";
        }
        return label;
    }

}
