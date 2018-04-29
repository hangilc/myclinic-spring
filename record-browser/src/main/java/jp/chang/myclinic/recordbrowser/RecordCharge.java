package jp.chang.myclinic.recordbrowser;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ChargeDTO;

class RecordCharge extends TextFlow {

   // private static Logger logger = LoggerFactory.getLogger(RecordCharge.class);

    RecordCharge(ChargeDTO charge) {
        String s = getChargeText(charge);
        getChildren().add(new Text(s));
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
