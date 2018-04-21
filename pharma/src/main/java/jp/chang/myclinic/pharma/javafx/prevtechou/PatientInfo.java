package jp.chang.myclinic.pharma.javafx.prevtechou;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PatientInfo extends TextFlow {

    private static Logger logger = LoggerFactory.getLogger(PatientInfo.class);
    private Text text = new Text("");

    PatientInfo() {
        getChildren().add(text);
        updateVisibility(false);
    }

    void setPatient(PatientDTO patient){
        if( patient == null ){
            updateVisibility(false);
        } else {
            text.setText(String.format("(%d) %s %s", patient.patientId,
                    patient.lastName, patient.firstName));
            updateVisibility(true);
        }
    }

    private void updateVisibility(boolean visible){
        setManaged(visible);
        setVisible(visible);
    }

}
