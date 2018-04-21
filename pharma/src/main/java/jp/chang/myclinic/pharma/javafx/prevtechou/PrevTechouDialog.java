package jp.chang.myclinic.pharma.javafx.prevtechou;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrevTechouDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(PrevTechouDialog.class);

    public PrevTechouDialog() {
        setTitle("過去のお薬手帳");
        Parent root = new PrevTechouRoot();
        root.getStyleClass().add("prev-techou-dialog");
        root.getStylesheets().add("Pharma.css");
        setScene(new Scene(root));
    }

}
