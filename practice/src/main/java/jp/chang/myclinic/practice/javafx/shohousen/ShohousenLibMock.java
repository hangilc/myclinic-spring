package jp.chang.myclinic.practice.javafx.shohousen;

import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.util.DateTimeUtil;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class ShohousenLibMock {

    public static ShohousenLib create(){
        ShohousenLibMock mock = new ShohousenLibMock();
        mock.ensurePrinterSettingDir();
        return mock.createLib();
    }

    private String tempPrinterSettingDir = "./work/myclinic-env/printer-settings";

    private void ensurePrinterSettingDir(){
        File file = new File(tempPrinterSettingDir);
        if( !file.exists() ) {
            boolean ok = file.mkdirs();
            if (!ok) {
                throw new RuntimeException("failed to create direcotry: " + tempPrinterSettingDir);
            }
        }
    }

    private ShohousenLib createLib(){
        MockData mock = new MockData();
        PatientDTO patient = mock.pickPatient();
        return new ShohousenLib(){
            @Override
            public PrinterEnv getPrinterEnv() {
                return new PrinterEnv(Paths.get(tempPrinterSettingDir));
            }

            @Override
            public String getPrinterSetting() {
                return "";
            }

            @Override
            public void setPrinterSetting(String newSetting) {
                // nop
            }

            @Override
            public CompletableFuture<ClinicInfoDTO> getClinicInfo() {
                ClinicInfoDTO info = new ClinicInfoDTO();
                info.name = "テストクリニック";
                info.postalCode = "123-4567";
                info.address = "某県某所１丁目２－３４";
                info.tel = "03-1234-8888";
                info.fax = "03-1234-7777";
                info.todoufukencode = "13";
                info.tensuuhyoucode = "1";
                info.kikancode = "0000000";
                info.homepage = "http://example.com";
                info.doctorName = "試験 データ";
                return CompletableFuture.completedFuture(info);
            }

            @Override
            public CompletableFuture<PatientDTO> getPatient(int patientId) {
                return CompletableFuture.completedFuture(patient);
            }

            @Override
            public CompletableFuture<VisitDTO> getVisit(int visitId) {
                VisitDTO visit = new VisitDTO();
                visit.visitId = visitId;
                visit.patientId = patient.patientId;
                visit.visitedAt = DateTimeUtil.toSqlDateTime(LocalDateTime.now());
                return CompletableFuture.completedFuture(visit);
            }

            @Override
            public CompletableFuture<HokenDTO> getHoken(int visitId) {
                HokenDTO hoken = new HokenDTO();
                return CompletableFuture.completedFuture(hoken);
            }
        };
    }

}
