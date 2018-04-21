package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NewPharmaDrugDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(NewPharmaDrugDialog.class);

    NewPharmaDrugDialog() {
        setTitle("新規薬剤情報入力");
        setScene(new Scene(new NewPharmaDrugRoot(){
            @Override
            protected void onEnter(int iyakuhincode) {
                NewPharmaDrugDialog.this.onEnter(iyakuhincode);
                clear();
            }

            @Override
            protected void onClose() {
                close();
            }
        }));
    }

    protected void onEnter(int iyakuhincode){

    }

}
