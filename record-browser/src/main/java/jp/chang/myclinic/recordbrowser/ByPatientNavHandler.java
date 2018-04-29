package jp.chang.myclinic.recordbrowser;

import javafx.application.Platform;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2PageDTO;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.utilfx.NavHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

class ByPatientNavHandler implements NavHandler {

    private static Logger logger = LoggerFactory.getLogger(ByPatientNavHandler.class);
    private PatientDTO patient;

    ByPatientNavHandler(PatientDTO patient) {
        this.patient = patient;
    }

    @Override
    public void trigger(int page, BiConsumer<Integer, Integer> cb) {
        Service.api.listVisitFull2(patient.patientId, page)
                .thenAccept(result -> Platform.runLater(() -> {
                    onPage(patient, result);
                    cb.accept(result.page, result.totalPages);
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    void onPage(PatientDTO patient, VisitFull2PageDTO pageContent){

    }
}
