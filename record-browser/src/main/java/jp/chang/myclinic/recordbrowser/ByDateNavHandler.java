package jp.chang.myclinic.recordbrowser;

import javafx.application.Platform;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.VisitFull2PatientPageDTO;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.utilfx.NavHandler;

import java.time.LocalDate;
import java.util.function.BiConsumer;

class ByDateNavHandler implements NavHandler {

    //private static Logger logger = LoggerFactory.getLogger(ByDateNavHandler.class);
    private LocalDate at;

    ByDateNavHandler(LocalDate at) {
        this.at = at;
    }

    @Override
    public void trigger(int page, BiConsumer<Integer, Integer> cb) {
        Service.api.pageVisitFullWithPatientAt(at.toString(), page)
                .thenAccept(result -> Platform.runLater(() -> {
                    onPage(result, at);
                    cb.accept(result.page, result.totalPages);
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    void onPage(VisitFull2PatientPageDTO pageContent, LocalDate at){

    }
}
