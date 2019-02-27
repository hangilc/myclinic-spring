package jp.chang.myclinic.practice.javafx.shohousen;

import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class ShohousenRequirement {

    public interface ShohousenRestService {
        CompletableFuture<PatientDTO> getPatient(int patientId);
        CompletableFuture<VisitDTO> getVisit(int visitId);
        CompletableFuture<HokenDTO> getHoken(int visitId);
    }

    public interface ShohousenConfigService {
        String getShohousenPrinterSetting();
        void setShohousenPrinterSetting(String settingName);
        PrinterEnv getPrinterEnv();
        ClinicInfoDTO getClinicInfo();
    }

    public ShohousenRestService restService;
    public ShohousenConfigService configService;

    public ShohousenRequirement(){

    }

    public ShohousenRequirement(ShohousenRestService restService, ShohousenConfigService configService){
        this.restService = restService;
        this.configService = configService;
    }
}
