package jp.chang.myclinic.practice.javafx.parts;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.PracticeHelper;
import jp.chang.myclinic.practice.javafx.parts.searchbox.SimpleSearchBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PatientSearchBox extends SimpleSearchBox<PatientDTO> {


    public PatientSearchBox() {
        super(PatientSearchBox::search, PatientSearchBox::convert);
    }

    private static CompletableFuture<List<PatientDTO>> search(String text){
        PracticeHelper helper = new PracticeHelper();
        return helper.searchPatient(text);
    }

    private static String convert(PatientDTO patient){
        return String.format("[%04d] %s %s", patient.patientId, patient.lastName, patient.firstName);
    }

}
