package jp.chang.myclinic.practice.lib.dateinput;

import javafx.application.Platform;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2PageDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.lib.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class PracticeLib {

    private static Logger logger = LoggerFactory.getLogger(PracticeLib.class);

    public static void startPatient(int patientId, Runnable cb){
        CompletableFuture<PatientDTO> patientFuture = Service.api.getPatient(patientId);
        try {
            PatientDTO patient = patientFuture.join();
            startPatient(patient, cb);
        } catch(Exception ex){
            logger.error("Failed start patient.", ex);
            Platform.runLater(() -> GuiUtil.alertException("患者を開始できませんでした。", ex));
        }
    }

    public static void startPatient(PatientDTO patient, Runnable cb){
        CompletableFuture<VisitFull2PageDTO> visitsFuture = Service.api.listVisitFull2(patient.patientId, 0);
        try {
            VisitFull2PageDTO visits = visitsFuture.join();
            PracticeEnv env = PracticeEnv.INSTANCE;
            Platform.runLater(() -> {
                env.setCurrentPatient(patient);
                env.setTotalRecordPages(visits.totalPages);
                env.setCurrentRecordPage(visits.page);
                env.setPageVisits(visits.visits);
                cb.run();
            });
        } catch(Exception ex){
            logger.error("Failed start patient.", ex);
            Platform.runLater(() -> GuiUtil.alertException("患者を開始できませんでした。", ex));
        }
    }
}
