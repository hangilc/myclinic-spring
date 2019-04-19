package jp.chang.myclinic.practice;

import javafx.application.Platform;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.frontend.Frontend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class PracticeHelper {

    private static PracticeHelper INSTANCE = new PracticeHelper();

    public static PracticeHelper getInstance() {
        return INSTANCE;
    }

    public LocalDate getVisitedAt(String visitedAt) {
        if (visitedAt.length() == 10) {
            return LocalDate.parse(visitedAt);
        } else {
            return LocalDateTime.parse(visitedAt).toLocalDate();
        }
    }

    public CompletableFuture<Void> endPatient() {
        CurrentPatientService curr = Context.currentPatientService;
        Frontend frontend = Context.frontend;
        int currentVisitId = curr.getCurrentVisitId();
        return (currentVisitId == 0 ?
                CompletableFuture.completedFuture(null) :
                frontend.suspendExam(currentVisitId))
                .thenAcceptAsync(v -> {
                    curr.setCurrentPatient(null, 0);
                    Context.integrationService.broadcastVisitPage(0, 0, Collections.emptyList());
                }, Platform::runLater);
    }

    public CompletableFuture<Void> startPatient(PatientDTO patient) {
        Frontend frontend = Context.frontend;
        return endPatient()
                .thenCompose(v -> {
                    Context.currentPatientService.setCurrentPatient(patient, 0);
                    return frontend.listVisitFull2(patient.patientId, 0);
                })
                .thenAcceptAsync(result -> Context.integrationService.broadcastVisitPage(
                        result.page, result.totalPages, result.visits
                        ),
                        Platform::runLater);
    }

}
