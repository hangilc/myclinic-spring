package jp.chang.myclinic.practice;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class PracticeEnv {

    private static final Logger logger = LoggerFactory.getLogger(PracticeEnv.class);

    public static PracticeEnv INSTANCE;

    private Path printerSettingsDir;
    private ClinicInfoDTO clinicInfo;
    private ObjectProperty<PatientDTO> currentPatient = new SimpleObjectProperty<>();
    private IntegerProperty currentVisitId = new SimpleIntegerProperty(0);
    private IntegerProperty tempVisitId = new SimpleIntegerProperty(0);
    private int recordsPerPage = 10;
    private IntegerProperty totalRecordPages = new SimpleIntegerProperty(0);
    private IntegerProperty currentRecordPage = new SimpleIntegerProperty(0);
    private ObjectProperty<List<VisitFull2DTO>> pageVisits = new SimpleObjectProperty<>(Collections.emptyList());
    private ObjectProperty<List<DiseaseFullDTO>> currentDiseases = new SimpleObjectProperty<>(Collections.emptyList());

    public PracticeEnv(CommandArgs commandArgs){
        printerSettingsDir = commandArgs.getWorkingDirectory();
        if( printerSettingsDir == null ){
            printerSettingsDir = Paths.get(System.getProperty("user.home"), "practice-home");
        }
        if( !(Files.exists(printerSettingsDir) && Files.isDirectory(printerSettingsDir)) ){
            logger.error("Invalid printer settings directory: " + printerSettingsDir);
            System.exit(1);
        }
    }

    public Path getPrinterSettingsDir() {
        return printerSettingsDir;
    }

    public void setPrinterSettingsDir(Path printerSettingsDir) {
        this.printerSettingsDir = printerSettingsDir;
    }

    public ClinicInfoDTO getClinicInfo() {
        return clinicInfo;
    }

    public void setClinicInfo(ClinicInfoDTO clinicInfo) {
        this.clinicInfo = clinicInfo;
    }

    public PatientDTO getCurrentPatient() {
        return currentPatient.get();
    }

    public ObjectProperty<PatientDTO> currentPatientProperty() {
        return currentPatient;
    }

    public void setCurrentPatient(PatientDTO currentPatient) {
        this.currentPatient.set(currentPatient);
    }

    public int getRecordsPerPage(){
        return recordsPerPage;
    }

    public int getTotalRecordPages() {
        return totalRecordPages.get();
    }

    public IntegerProperty totalRecordPagesProperty() {
        return totalRecordPages;
    }

    public void setTotalRecordPages(int totalRecordPages) {
        this.totalRecordPages.set(totalRecordPages);
    }

    public int getCurrentRecordPage() {
        return currentRecordPage.get();
    }

    public IntegerProperty currentRecordPageProperty() {
        return currentRecordPage;
    }

    public void setCurrentRecordPage(int currentRecordPage) {
        this.currentRecordPage.set(currentRecordPage);
    }

    public List<VisitFull2DTO> getPageVisits() {
        return pageVisits.get();
    }

    public ObjectProperty<List<VisitFull2DTO>> pageVisitsProperty() {
        return pageVisits;
    }

    public void setPageVisits(List<VisitFull2DTO> pageVisits) {
        this.pageVisits.set(pageVisits);
    }

    public List<DiseaseFullDTO> getCurrentDiseases() {
        return currentDiseases.get();
    }

    public ObjectProperty<List<DiseaseFullDTO>> currentDiseasesProperty() {
        return currentDiseases;
    }

    public void setCurrentDiseases(List<DiseaseFullDTO> currentDiseases) {
        this.currentDiseases.set(currentDiseases);
    }

    public int getCurrentVisitId() {
        return currentVisitId.get();
    }

    public IntegerProperty currentVisitIdProperty() {
        return currentVisitId;
    }

    public void setCurrentVisitId(int currentVisitId) {
        this.currentVisitId.set(currentVisitId);
    }

    public int getTempVisitId() {
        return tempVisitId.get();
    }

    public IntegerProperty tempVisitIdProperty() {
        return tempVisitId;
    }

    public void setTempVisitId(int tempVisitId) {
        this.tempVisitId.set(tempVisitId);
    }

    @Override
    public String toString() {
        return "PracticeEnv{" +
                "printerSettingsDir=" + printerSettingsDir +
                ", clinicInfo=" + clinicInfo +
                '}';
    }
}
