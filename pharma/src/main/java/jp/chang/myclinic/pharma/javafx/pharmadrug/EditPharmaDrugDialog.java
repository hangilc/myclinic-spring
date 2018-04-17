package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditPharmaDrugDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(EditPharmaDrugDialog.class);

    public EditPharmaDrugDialog() {
        setTitle("新規薬剤情報の編集");
        setScene(new Scene(new EditPharmaDrugScene(){
            @Override
            protected void onEnter() {
                close();
            }
        }));

    }

}
