package jp.chang.myclinic.practice.lib;

import javafx.application.Platform;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2PageDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.Service;
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

    public static void gotoFirstRecordPage(){
        PracticeEnv env = PracticeEnv.INSTANCE;
        PatientDTO patient = env.getCurrentPatient();
        if( patient != null ){
            int total = env.getTotalRecordPages();
            if( total > 1 ){
                int curr = env.getCurrentRecordPage();
                if( curr > 0 ){
                    PracticeService.listVisits(patient.patientId, 0, visits -> {
                        env.setPageVisits(visits);
                        env.setCurrentRecordPage(0);
                    });
                }
            }
        }
    }

    public static void gotoPrevRecordPage(){
        PracticeEnv env = PracticeEnv.INSTANCE;
        PatientDTO patient = env.getCurrentPatient();
        if( patient != null ){
            int total = env.getTotalRecordPages();
            if( total > 1 ){
                int curr = env.getCurrentRecordPage();
                int page = curr - 1;
                if( page >= 0 ){
                    PracticeService.listVisits(patient.patientId, page, visits -> {
                        env.setPageVisits(visits);
                        env.setCurrentRecordPage(page);
                    });
                }
            }
        }
    }

    public static void gotoNextRecordPage(){
        PracticeEnv env = PracticeEnv.INSTANCE;
        PatientDTO patient = env.getCurrentPatient();
        if( patient != null ){
            int total = env.getTotalRecordPages();
            if( total > 1 ){
                int curr = env.getCurrentRecordPage();
                int page = curr + 1;
                if( page < total ){
                    PracticeService.listVisits(patient.patientId, page, visits -> {
                        env.setPageVisits(visits);
                        env.setCurrentRecordPage(page);
                    });
                }
            }
        }
    }

    public static void gotoLastRecordPage(){
        PracticeEnv env = PracticeEnv.INSTANCE;
        PatientDTO patient = env.getCurrentPatient();
        if( patient != null ){
            int total = env.getTotalRecordPages();
            if( total > 1 ){
                int curr = env.getCurrentRecordPage();
                if( curr < total - 1 ){
                    PracticeService.listVisits(patient.patientId, total - 1, visits -> {
                        env.setPageVisits(visits);
                        env.setCurrentRecordPage(total - 1);
                    });
                }
            }
        }
    }

}
