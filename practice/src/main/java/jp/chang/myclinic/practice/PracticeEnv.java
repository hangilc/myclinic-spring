package jp.chang.myclinic.practice;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.myclinicenv.MyclinicEnv;
import jp.chang.myclinic.practice.javafx.ShoukiForm;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

public class PracticeEnv {

    public static final String APP_NAME = "practice";
    public static final String SHOHOUSEN_PRINTER_SETTING_KEY = "shohousen-printer-setting";
    public static final String REFER_PRINTER_SETTING_KEY = "refer-printer-setting";
    public static PracticeEnv INSTANCE = new PracticeEnv();

    //private Path printerSettingsDir;
    private ObjectProperty<PatientDTO> currentPatient = new SimpleObjectProperty<>();
    private IntegerProperty currentVisitId = new SimpleIntegerProperty(0);
    private IntegerProperty tempVisitId = new SimpleIntegerProperty(0);
    private static final int recordsPerPage = 10;
    private IntegerProperty totalRecordPages = new SimpleIntegerProperty(0);
    private IntegerProperty currentRecordPage = new SimpleIntegerProperty(0);
    private ObjectProperty<List<VisitFull2DTO>> pageVisits = new SimpleObjectProperty<>(Collections.emptyList());
    private Map<Integer, ShoukiForm> shoukiFormMap = new HashMap<>();
    private List<BiConsumer<Integer, ShoukiDTO>> shoukiChangeListeners = new ArrayList<>();
    private MyclinicEnv myclinicEnv;
    private List<ReferItemDTO> referList;
    private String kouhatsuKasan;

    public PracticeEnv(){
        myclinicEnv = new MyclinicEnv(APP_NAME);
        currentPatient.addListener((obs, oldValue, newValue) -> {
            shoukiFormMap.values().forEach(Stage::close);
            shoukiFormMap.clear();
        });
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

    public int getRecordsPerPage() {
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

    public boolean isCurrentOrTempVisitId(int visitId) {
        return visitId == Context.currentPatientService.getCurrentOrTempVisitId();
    }

    public MyclinicEnv getMyclinicEnv() {
        return myclinicEnv;
    }

    public Properties getAppProperties() throws IOException {
        return getMyclinicEnv().getAppProperties();
    }

    public String getAppProperty(String key) throws IOException {
        return getAppProperties().getProperty(key);
    }

    public void setAppProperty(String key, String value) throws IOException {
        Properties props = getAppProperties();
        if( value == null ){
            props.remove(key);
        } else {
            props.setProperty(key, value);
        }
        saveAppProperties(props);
    }

    public void saveAppProperties(Properties properties) throws IOException {
        getMyclinicEnv().saveAppProperties(properties);
    }

    public List<ReferItemDTO> getReferList() {
        return referList;
    }

    public void setReferList(List<ReferItemDTO> referList) {
        this.referList = referList;
    }

    public String getKouhatsuKasan() {
        return kouhatsuKasan;
    }

    public void setKouhatsuKasan(String kouhatsuKasan) {
        this.kouhatsuKasan = kouhatsuKasan;
    }

    @Override
    public String toString() {
        return "PracticeEnv{" +
                ", currentPatient=" + currentPatient +
                ", currentVisitId=" + currentVisitId +
                ", tempVisitId=" + tempVisitId +
                ", totalRecordPages=" + totalRecordPages +
                ", currentRecordPage=" + currentRecordPage +
                ", pageVisits=" + pageVisits +
                ", myclinicEnv=" + myclinicEnv +
                ", referList=" + referList +
                ", kouhatsuKasan='" + kouhatsuKasan + '\'' +
                '}';
    }
}
