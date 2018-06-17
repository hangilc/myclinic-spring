package jp.chang.myclinic.recordbrowser;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

class ByDateDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(ByDateDialog.class);

    ByDateDialog(LocalDate date) {
        setTitle("診療録（日付別）");
        ByDateRoot root = new ByDateRoot();
        root.setDate(date);
        root.trigger();
        setScene(new Scene(root));
    }

}
