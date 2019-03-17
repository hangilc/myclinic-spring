package jp.chang.myclinic.backendpgsql;

import jp.chang.myclinic.backend.Persistence;
import jp.chang.myclinic.backendpgsql.table.*;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PgsqlPersistence implements Persistence {

    private PatientTable patientTable = new PatientTable();
    private VisitTable visitTable = new VisitTable();
    private ShahokokuhoTable shahokokuhoTable = new ShahokokuhoTable();
    private KoukikoureiTable koukikoureiTable = new KoukikoureiTable();
    private RoujinTable roujinTable = new RoujinTable();
    private KouhiTable kouhiTable = new KouhiTable();
    private WqueueTable wqueueTable = new WqueueTable();
    private ShinryouAttrTable shinryouAttrTable = new ShinryouAttrTable();
    private DrugAttrTable drugAttrTable = new DrugAttrTable();
    private ShoukiTable shoukiTable = new ShoukiTable();
    private TextTable textTable = new TextTable();
    private PracticeLogTable practiceLogTable = new PracticeLogTable();

    @Override
    public int enterPatient(PatientDTO patient) {
        patientTable.insert(patient);
        return patient.patientId;
    }

    @Override
    public PatientDTO getPatient(int patientId) {
        return patientTable.getById(patientId);
    }

    @Override
    public int enterVisit(VisitDTO visit) {
        visitTable.insert(visit);
        return visit.visitId;
    }

    @Override
    public VisitDTO getVisit(int visitId) {
        return visitTable.getById(visitId);
    }

    @Override
    public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at) {
        return shahokokuhoTable.findAvailable(patientId, at);
    }

    @Override
    public ShahokokuhoDTO getShahokokuho(int shahokokuhoId) {
        return shahokokuhoTable.getById(shahokokuhoId);
    }

    @Override
    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at) {
        return koukikoureiTable.findAvailable(patientId, at);
    }

    @Override
    public KoukikoureiDTO getKoukikourei(int koukikoureiId) {
        return koukikoureiTable.getById(koukikoureiId);
    }

    @Override
    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at) {
        return roujinTable.findAvailable(patientId, at);
    }

    @Override
    public RoujinDTO getRoujin(int roujinId) {
        return roujinTable.getById(roujinId);
    }

    @Override
    public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at) {
        return kouhiTable.findAvailable(patientId, at);
    }

    @Override
    public KouhiDTO getKouhi(int kouhiId) {
        return kouhiTable.getById(kouhiId);
    }

    @Override
    public void enterWqueue(WqueueDTO wqueue) {
        wqueueTable.insert(wqueue);
    }

    @Override
    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds) {
        List<ShinryouAttrDTO> result = new ArrayList<>();
        for(Integer shinryouId: shinryouIds){
            ShinryouAttrDTO dto = shinryouAttrTable.getById(shinryouId);
            if( dto != null ){
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        List<DrugAttrDTO> result = new ArrayList<>();
        for(Integer drugId: drugIds){
            DrugAttrDTO dto = drugAttrTable.getById(drugId);
            if( dto != null ){
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds) {
        List<ShoukiDTO> result = new ArrayList<>();
        for(Integer visitId: visitIds){
            ShoukiDTO dto = shoukiTable.getById(visitId);
            if( dto != null ){
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public int enterText(TextDTO text) {
        textTable.insert(text);
        return text.textId;
    }

    @Override
    public TextDTO getText(int textId) {
        return textTable.getById(textId);
    }

    @Override
    public void updateText(TextDTO text) {
        textTable.update(text);
    }

    @Override
    public void deleteText(int textId) {
        textTable.delete(textId);
    }

    @Override
    public List<TextDTO> listText(int visitId) {
        return textTable.listText(visitId);
    }

    @Override
    public void enterPracticeLog(PracticeLogDTO practiceLog) {
        practiceLogTable.insert(practiceLog);
    }

}
