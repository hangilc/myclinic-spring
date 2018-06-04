package jp.chang.myclinic.recordbrowser;

import javafx.application.Platform;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.VisitFull2PatientDTO;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.utilfx.NavHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class ByDateNavHandler implements NavHandler {

    //private static Logger logger = LoggerFactory.getLogger(ByDateNavHandler.class);
    private LocalDate at;
    private Consumer<List<VisitFull2PatientDTO>> pageCallback = page -> {};

    ByDateNavHandler(LocalDate at) {
        this.at = at;
    }

    @Override
    public void trigger(int page, BiConsumer<Integer, Integer> cb) {
        Service.api.pageVisitFullWithPatientAt(at.toString(), page)
                .thenAccept(result -> Platform.runLater(() -> {
                    pageCallback.accept(result.visitPatients);
                    cb.accept(result.page, result.totalPages);
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    void setDate(LocalDate date){
        this.at = date;
    }

    LocalDate getDate(){
        return at;
    }

    void setPageCallback(Consumer<List<VisitFull2PatientDTO>> cb){
        this.pageCallback = cb;
    }

}
