package jp.chang.myclinic.practice.javafx.shohousen;

import javafx.application.Platform;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.mockdata.MockPrinterEnv;
import jp.chang.myclinic.mockdata.SampleData;
import jp.chang.myclinic.practice.testgui.TestGroup;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class TestShohousen extends TestGroup {

    {
        addTestProcSingleOnly("disp", this::testDisp);
    }

    private void testDisp(){
        MockData mock = new MockData();
        PatientDTO patient = mock.pickPatientWithPatientId();
        VisitDTO visit = mock.pickVisitWithVisitId(patient.patientId, LocalDateTime.now());
        HokenDTO hoken = new HokenDTO();
        ShohousenRequirement requirement = new ShohousenRequirement();
        requirement.restService = new ShohousenRequirement.ShohousenRestService() {
            @Override
            public CompletableFuture<PatientDTO> getPatient(int patientId) {
                return CompletableFuture.completedFuture(patient);
            }

            @Override
            public CompletableFuture<VisitDTO> getVisit(int visitId) {
                return CompletableFuture.completedFuture(visit);
            }

            @Override
            public CompletableFuture<HokenDTO> getHoken(int visitId) {
                return CompletableFuture.completedFuture(hoken);
            }
        };
        requirement.configService = new ShohousenRequirement.ShohousenConfigService() {
            @Override
            public String getShohousenPrinterSetting() {
                return null;
            }

            @Override
            public void setShohousenPrinterSetting(String settingName) {

            }

            @Override
            public PrinterEnv getPrinterEnv() {
                return MockPrinterEnv.create();
            }

            @Override
            public ClinicInfoDTO getClinicInfo() {
                return SampleData.sampleClinicInfo;
            }
        };
        ShohousenPreview.create(requirement, visit.visitId, "Ｒｐ）")
                .thenAcceptAsync(Stage::show, Platform::runLater).join();
    }

}
