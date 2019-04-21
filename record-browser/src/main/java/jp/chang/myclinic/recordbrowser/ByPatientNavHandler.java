package jp.chang.myclinic.recordbrowser;

import javafx.application.Platform;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.utilfx.NavHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class ByPatientNavHandler implements NavHandler {

    private static Logger logger = LoggerFactory.getLogger(ByPatientNavHandler.class);
    private PatientDTO patient;
    private Consumer<List<VisitFull2DTO>> pageCallback = visits -> {};

    ByPatientNavHandler(PatientDTO patient) {
        this.patient = patient;
    }

    @Override
    public void trigger(int page, BiConsumer<Integer, Integer> cb) {
        if( patient != null ) {
            Service.api.listVisitFull2(patient.patientId, page)
                    .thenAccept(result -> Platform.runLater(() -> {
                        pageCallback.accept(result.visits);
                        cb.accept(result.page, result.totalPages);
                    }))
                    .exceptionally(HandlerFX.exceptionally());
        } else {
            pageCallback.accept(Collections.emptyList());
        }
    }

    public void setPageCallback(Consumer<List<VisitFull2DTO>> pageCallback){
        this.pageCallback = pageCallback;
    }

}
