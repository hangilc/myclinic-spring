package jp.chang.myclinic.recordbrowser;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;

public class PatientHistoryDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(PatientHistoryDialog.class);

    public PatientHistoryDialog(PatientDTO patient) {
        setTitle(String.format("診療録（%s%s）", patient.lastName, patient.firstName));
        PatientHistoryRoot root = new PatientHistoryRoot(patient);
        setScene(new Scene(root));
        root.setupOnClose(this);
        root.trigger();
    }

}
