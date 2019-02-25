package jp.chang.myclinic.practice.javafx.shohousen;

import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;

import java.util.concurrent.CompletableFuture;

public interface ShohousenLib {

    default PrinterEnv getPrinterEnv(){ throw new RuntimeException("not implemented"); }
    default String getPrinterSetting(){ throw new RuntimeException("not implemented"); }
    default void setPrinterSetting(String newSetting){ throw new RuntimeException("not implemented"); }
    default CompletableFuture<ClinicInfoDTO> getClinicInfo(){ throw new RuntimeException("not implemented"); }
    default CompletableFuture<PatientDTO> getPatient(int patientId){ throw new RuntimeException("not implemented"); }
    default CompletableFuture<VisitDTO> getVisit(int visitId){ throw new RuntimeException("not implemented"); }
    default CompletableFuture<HokenDTO> getHoken(int visitId){ throw new RuntimeException("not implemented"); }

}
