package jp.chang.myclinic.recordbrowser;

import javafx.application.Platform;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.VisitFull2PatientDTO;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.utilfx.NavHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class ByDateNavHandler implements NavHandler {

    private static Logger logger = LoggerFactory.getLogger(ByDateNavHandler.class);
    private LocalDate at;
    private Consumer<List<VisitFull2PatientDTO>> pageCallback = page -> {
    };
    private int serialId = 0;
    private int completedId = 0;

    ByDateNavHandler(LocalDate at) {
        this.at = at;
    }

    @Override
    public void trigger(int page, BiConsumer<Integer, Integer> cb) {
        final int ser = nextSerialId();
        logger.info("triggered");
        Service.api.pageVisitFullWithPatientAt(at.toString(), page)
                .thenAccept(result -> {
                    if( complete(ser) ) {
                        logger.info("UI updated");
                        Platform.runLater(() -> {
                            pageCallback.accept(result.visitPatients);
                            cb.accept(result.page, result.totalPages);
                        });
                    }
                })
                .exceptionally(HandlerFX::exceptionally);
    }

    private int nextSerialId() {
        synchronized (this) {
            serialId += 1;
            return serialId;
        }
    }

    private boolean complete(int ser) {
        synchronized (this) {
            if (completedId < ser) {
                completedId = ser;
                return true;
            } else {
                return false;
            }
        }
    }

    void setDate(LocalDate date) {
        this.at = date;
    }

    LocalDate getDate() {
        return at;
    }

    void setPageCallback(Consumer<List<VisitFull2PatientDTO>> cb) {
        this.pageCallback = cb;
    }

}
