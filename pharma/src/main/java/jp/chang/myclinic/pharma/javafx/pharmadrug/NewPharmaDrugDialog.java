package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewPharmaDrugDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(NewPharmaDrugDialog.class);

    public NewPharmaDrugDialog() {
        setTitle("新規薬剤情報入力");
        setScene(new Scene(new NewPharmaDrugScene()));
    }

}
